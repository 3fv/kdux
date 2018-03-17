package org.densebrain.kdux.store

import org.densebrain.kdux.actions.Actions
import kotlin.reflect.KClass

object StoreContext {

  /**
   * Internal ref to store
   */
  private var internalStore: Store? = null

  /**
   * Check if the store is ready
   */
  private fun checkStoreReady() {
    if (internalStore == null)
      throw IllegalStateException("Store is not set yet")
  }

  /**
   * Store getter
   */
  val store: Store
    get() {
      checkStoreReady()
      return internalStore!!
    }


  /**
   * Shortcut to constructor
   */
  fun configure(vararg actionClazzes: KClass<*>) = configure(Store(*actionClazzes))

  /**
   * Configure the context
   */
  fun configure(store: Store): StoreContext {
    internalStore = store
    return this
  }
}

/**
 * Single updater callback instead of getter - returns whole state
 */
inline fun <reified T : State> observe(
  crossinline updater: StoreObserverUpdater<T, T>
): StoreObserver<T, T> = StoreContext.store.observe(updater, { state: T -> state })

/**
 * Shortcut to observe
 */
inline fun <reified R,reified T : State> observe(
  crossinline updater: StoreObserverUpdater<R, T>,
  crossinline getter: StoreObserverGetter<R, T>
): StoreObserver<R, T> = StoreContext.store.observe(updater, getter)


/**
 * Get state
 */
inline fun <reified S: State> state() = StoreContext.store.getLeafState(S::class)


/**
 * Get actions
 */
inline fun <reified A: Actions<*>> actions() = StoreContext.store.getActions<A>()


/**
 * Get actions
 */
inline fun <reified A: Actions<*>> getActions() = StoreContext.store.getActions<A>()
