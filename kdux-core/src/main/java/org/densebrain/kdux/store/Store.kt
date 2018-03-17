package org.densebrain.kdux.store

import org.densebrain.kdux.reducers.Reducer
import org.densebrain.kdux.actions.Actions
import arrow.effects.DeferredKW
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.immutableMapOf
import kotlinx.collections.immutable.mutate
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
typealias RootState = ImmutableMap<KClass<*>, State>

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
  private val observers = LinkedHashSet<StoreObserver<*, *>>()

  /**
   * Internal map of all leafs
   */
  private var rootState: RootState = immutableMapOf()

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
    val newRootState = rootState.mutate { rootState ->
      val reducersByState = pendingReducers.groupBy { it.first.stateType }
      reducersByState
        .entries
        .forEach { (stateType, reducers) ->
          var state = rootState[stateType]!!
          reducers.forEach { reducer ->
            @Suppress("UNCHECKED_CAST")
            state = (reducer.second as Reducer<Any>).handle(state) as State
            rootState[stateType] = state
          }
        }
    }

    rootState = newRootState

    updateScheduler.schedule(DefaultStoreUpdate(newRootState))
  }

  /**
   * Schedule processing
   */
  private fun scheduleProcess() {
    DeferredKW {
      process()
    }
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
    lateinit var observers: List<StoreObserver<*, *>>
    synchronized(this.observers) {
      observers = this.observers.toList()
    }

    observers.forEach { it.run(rootState) }
  }

  /**
   * Remove an observer
   */
  fun removeObserver(observer: StoreObserver<*, *>) {
    synchronized(observers) {
      observers.remove(observer)
    }
  }

  /**
   * Add an observer
   */
  fun <T: State, R> addObserver(observer: StoreObserver<R, T>): StoreObserver<R, T> {
    synchronized(observers) {
      if (!observers.contains(observer))
        observers.add(observer)
    }

    return observer
  }

  /**
   * Is observer attached
   */
  fun isObserverAttached(observer: StoreObserver<*, *>): Boolean {
    synchronized(observers) {
      return observers.contains(observer)
    }
  }

  /**
   * Simplified observer creation
   */
  inline fun <reified T: State, reified R> observe(
    crossinline updater: StoreObserverUpdater<R, T>,
    crossinline getter: StoreObserverGetter<R, T> = { state:T -> state as R }
  ): StoreObserver<R, T> = addObserver(StoreObserver<R, T>(
    this,
    T::class,
    R::class,
    { state: T -> getter(state) },
    { newValue: R, state: T, store: Store -> updater(newValue, state, store) }
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
    rootState = rootState.mutate {
      @Suppress("UNCHECKED_CAST")
      val actions = actionClazzes.map { getActions(it as KClass<Actions<*>>) }

      for (action in actions) {
        if (it[action.stateType] != null)
          continue

        val leafState = action.createState() ?: action.stateType.java.newInstance()
        it[action.stateType] = leafState
      }
    }
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