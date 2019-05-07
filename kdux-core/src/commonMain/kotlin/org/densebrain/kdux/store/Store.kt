package org.densebrain.kdux.store

import org.densebrain.kdux.reducers.Reducer
import org.densebrain.kdux.actions.Actions
import org.densebrain.kdux.util.Reflection
import org.densebrain.kdux.util.invokeSynchronized
import org.densebrain.kdux.util.shallowEquals
import kotlin.collections.LinkedHashSet
import kotlin.reflect.KClass

/**
 * Pending action
 */
typealias PendingActionReducer = Pair<Actions<*>, Reducer<*>>


/**
 * Root State type
 */
open class RootState(map: Map<KClass<*>, State> = mapOf()) : Map<KClass<*>, State> by map, State

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
open class MutableRootState(map: MutableMap<KClass<*>, State> = mutableMapOf()) : RootState(map),MutableMap<KClass<*>, State> by map

fun Map<KClass<*>, State>.toRootState() = RootState(this)

fun MutableMap<KClass<*>, State>.toMutableRootState() = MutableRootState(this)

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
  protected val observers = LinkedHashSet<StoreObserver<*>>()

  /**
   * Internal map of all leafs
   */
  private var rootState: RootState = RootState()

  /**
   * Internal map for Actions
   */
  private val actionMap = mutableMapOf<KClass<*>, Actions<*>>()

  /**
   * Pending reducers
   */
  //private val pendingReducers = ConcurrentLinkedDeque<PendingActionReducer>()

  /**
   * Process pending reducers
   */
  @Suppress("UNCHECKED_CAST")
  private fun process(actions: Actions<*>, reducer: Reducer<*>) {

    // RUN REDUCERS
    val newRootState = MutableRootState(rootState.toMutableMap())

    val stateType = actions.stateType
    val state = newRootState[stateType]!!
    val newState = (reducer as Reducer<Any>).handle(state) as State

    if (state !== newState && !shallowEquals(stateType as KClass<State>,state ,newState )) {
      newRootState[stateType] = newState
      rootState = newRootState.toRootState()
      updateScheduler.schedule(makeStoreUpdate(newRootState))
    }
  }

  open fun makeStoreUpdate(rootState:RootState):StoreUpdate {
    return DefaultStoreUpdate(rootState)
  }


  /**
   * Push updates to observers
   */
  protected open fun update(rootState: RootState) {
    lateinit var observers: Set<StoreObserver<*>>
    invokeSynchronized(this.observers) {
      observers = this.observers.toSet()
    }

    observers.forEach {
      it.run(rootState)
    }
  }

  /**
   * Remove an observer
   */
  fun removeObserver(observer: StoreObserver<*>) {
    invokeSynchronized(observers) {
      observers.remove(observer)
    }
  }

  /**
   * Add an observer
   */
  fun <R> addObserver(observer: StoreObserver<R>): StoreObserver<R> {
    invokeSynchronized(observers) {
      if (!observers.contains(observer))
        observers.add(observer)
    }

    return observer
  }

  /**
   * Is observer attached
   */
  fun isObserverAttached(observer: StoreObserver<*>): Boolean {
    return observers.contains(observer)
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
    process(actions, reducer)
  }

  /**
   * Get actions
   */
  @Suppress("UNCHECKED_CAST")
  fun <A : Actions<*>> getActions(clazz: KClass<A>): A {
    return actionMap.getOrPut(clazz) {
      val actions = Reflection.objectInstance(clazz)
      actions.setStore(this)
      actions
    } as A
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

      val leafState = action.createState() ?: Reflection.objectInstance(action.stateType)
      newRootState[action.stateType] = leafState
    }

    rootState = newRootState.toRootState()


  }

  /**
   * Default implementation of store update
   */
  protected inner class DefaultStoreUpdate(private val rootState: RootState) : StoreUpdate {
    override val store: Store
      get() = this@Store

    override fun run() {
      update(rootState)
    }
  }

}