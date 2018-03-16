package ai.mapper.kdux.actions

import ai.mapper.kdux.reducers.Reducer
import ai.mapper.kdux.reducers.ReducerType
import ai.mapper.kdux.store.State
import ai.mapper.kdux.store.Store

abstract class AbstractActions<S: State> : Actions<S> {

  /**
   * Private ref to store
   */
  private var store: Store? = null

  /**
   * Set the store ref
   */
  override fun setStore(store: Store) {
    this.store = store
  }

  /**
   * Get the store ref
   */
  override fun getStore(): Store {
    return this.store ?: error("Can not get store until set")
  }

  /**
   * Get the state for this set of Actions
   */
  val state:S
    get() = getStore().getLeafState(stateType)

  /**
   * Easy way to create a reducer in an actions class fully typed
   */
  fun reducer(body: ReducerType<S>): Reducer<S> = ai.mapper.kdux.reducers.reducer(body)
}