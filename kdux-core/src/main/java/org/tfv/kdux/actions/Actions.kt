package org.tfv.kdux.actions

import org.tfv.kdux.store.State
import org.tfv.kdux.store.Store
import kotlin.reflect.KClass

interface Actions<S: State> {

  /**
   * Get the state type
   */
  val stateType: KClass<S>

  /**
   * Create state instance, if NULL then newInstance is used
   * on KClass
   */
  fun createState():S? = null

  /**
   * Set the store
   */
  fun setStore(store: Store)

  /**
   * Get the store
   */
  fun getStore(): Store

}