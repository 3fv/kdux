package org.densebrain.kdux.store

import org.densebrain.kdux.reducers.Reducer
import org.densebrain.kdux.actions.Actions
import java.util.concurrent.ConcurrentLinkedDeque
import kotlin.collections.LinkedHashSet
import kotlin.reflect.KClass

/**
 * Pending action
 */
typealias PendingActionReducer = Pair<Actions<*>, Reducer<*>>

/**
 * Root State type
 */
typealias RootState = Map<KClass<*>, State>

typealias StateSelector<T,R> = (state:T) -> R

open class Store(
  vararg actionClazzes: KClass<*>
) {


  /**
   * Update scheduler
   */
  @Suppress("MemberVisibilityCanBePrivate")
  var updateScheduler: StoreUpdateScheduler = DefaultStoreUpdateScheduler()

  /**
   * Observers to the store
   */
  private val observers = LinkedHashSet<StoreObserver<*>>()

  /**
   * Internal map of all leafs
   */
  private var rootState: RootState = mapOf()

  /**
   * Internal map for Actions
   */
  private val actionMap = mutableMapOf<KClass<*>, Actions<*>>()

  /**
   * Pending reducers
   */
  private val pendingReducers = ConcurrentLinkedDeque<PendingActionReducer>()

  /**
   * Process pending reducers
   */
  @Synchronized
  private fun process() {

    // GET REDUCERS AND CLEAR PENDING
    val pendingReducers = run {
      lateinit var reducers: List<PendingActionReducer>
      synchronized(pendingReducers) {
        reducers = arrayListOf(*pendingReducers.toTypedArray())
        pendingReducers.clear()
      }
      return@run reducers
    }

    // RUN REDUCERS
    val newRootState = rootState.toMutableMap()

    val reducersByState = pendingReducers.groupBy { it.first.stateType }
    reducersByState
      .entries
      .forEach { (stateType, reducers) ->
        var state = newRootState[stateType]!!
        for (reducer in reducers) {
          @Suppress("UNCHECKED_CAST")
          state = (reducer.second as Reducer<Any>).handle(state) as State
        }

        newRootState[stateType] = state
      }

    rootState = newRootState.toMap()

    updateScheduler.schedule(DefaultStoreUpdate(newRootState))
  }

  /**
   * Schedule processing
   */
  private fun scheduleProcess() {
    process()
//    DeferredK {
//
//    }
  }

  /**
   * Push pending action reducer
   */
  private fun push(actions: Actions<*>, reducer: Reducer<*>) {
    synchronized(pendingReducers) {
      pendingReducers.push(Pair(actions, reducer))
    }

    scheduleProcess()
  }

  /**
   * Push updates to observers
   */
  @Synchronized
  private fun update(rootState: RootState) {
    lateinit var observers: List<StoreObserver<*>>
    synchronized(this.observers) {
      observers = this.observers.toList()
    }

    observers.forEach { it.run(rootState) }
  }

  /**
   * Remove an observer
   */
  fun removeObserver(observer: StoreObserver<*>) {
    synchronized(observers) {
      observers.remove(observer)
    }
  }

  /**
   * Add an observer
   */
  fun <R> addObserver(observer: StoreObserver<R>): StoreObserver<R> {
    synchronized(observers) {
      if (!observers.contains(observer))
        observers.add(observer)
    }

    return observer
  }

  /**
   * Is observer attached
   */
  fun isObserverAttached(observer: StoreObserver<*>): Boolean {
    synchronized(observers) {
      return observers.contains(observer)
    }
  }

  /**
   * Simplified observer creation
   */
  inline fun <reified T: State, reified R> observe(
    crossinline updater: StoreUpdateHandler<R>,
    crossinline getter: StoreSelector<T,R> = { state:T -> state as R }
  ): StoreObserver<R> = addObserver(DefaultStoreObserver(
    this,
    T::class,
    R::class,
    { state: T -> getter(state) },
    { newValue: R, oldValue: R? -> updater(newValue, oldValue) }
  ))


  /**
   * Get a leaf state
   */
  @Suppress("UNCHECKED_CAST")
  fun <S : State> getLeafState(stateType: KClass<S>) = rootState[stateType]!! as S

  /**
   * Get the root state
   */
  fun getRootState() = rootState

  /**
   * Dispatch an action
   */
  fun dispatch(actions: Actions<*>, reducer: Reducer<*>) {
    push(actions, reducer)
    scheduleProcess()
  }

  /**
   * Get actions
   */
  @Suppress("UNCHECKED_CAST")
  fun <A : Actions<*>> getActions(clazz: KClass<A>): A {
    return actionMap[clazz] as A? ?: run {
      val actions = when {
        clazz.isAbstract -> {
          val clazzName = "${clazz.java.name}Generated"
          actionMap[clazz] = Class.forName(clazzName).newInstance() as A
          actionMap[clazz] as A
        }
        else -> clazz.java.newInstance()
      }

      actions.setStore(this)

      return@run actions
    }
  }

  /**
   * Cute 1-liner reified
   */
  inline fun <reified A : Actions<*>> getActions(): A = getActions(A::class)

  /**
   * Initializer
   */
  init {
    val newRootState = rootState.toMutableMap()
    @Suppress("UNCHECKED_CAST")
    val actions = actionClazzes.map { getActions(it as KClass<Actions<*>>) }

    for (action in actions) {
      if (newRootState[action.stateType] != null)
        continue

      val leafState = action.createState() ?: action.stateType.java.newInstance()
      newRootState[action.stateType] = leafState
    }

    rootState = newRootState.toMap()

  }

  /**
   * Default implementation of store update
   */
  private inner class DefaultStoreUpdate(val rootState: RootState) : StoreUpdate {
    override fun run() {
      update(rootState)
    }
  }
}