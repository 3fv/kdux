package org.tfv.kdux.store

import org.tfv.kdux.actions.Actions
import org.tfv.kdux.util.Event
import kotlin.properties.Delegates
import kotlin.reflect.KClass

class StoreReadyEvent

object StoreContext {

  object events {
    val ready = Event<StoreReadyEvent>()
  }

  /**
   * Internal ref to store
   */
  private var internalStore by Delegates.observable<Store?>(null) { _, _, _ ->
    events.ready.emit(StoreReadyEvent())
  }

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
  crossinline updater: StoreUpdateHandler<T>
): StoreObserver<T> = StoreContext.store.observe(updater, { state: T -> state })

/**
 * Shortcut to observe
 */
inline fun <reified T : State, reified R> observe(
  crossinline updater: StoreUpdateHandler<R>,
  crossinline getter: StoreSelector<T, R>
): StoreObserver<R> = StoreContext.store.observe(updater, getter)


/**
 * Get state
 */
inline fun <reified S : State> state() = StoreContext.store.getLeafState(S::class)


/**
 * Get actions
 */
inline fun <reified A : Actions<*>> actions() = StoreContext.store.getActions<A>()


/**
 * Get actions
 */
inline fun <reified A : Actions<*>> getActions() = StoreContext.store.getActions<A>()


/**
 * Builder for observations
 */
open class StoreObserversBuilder {


  /**
   * Keep all builders handy
   */
  val observerBuilders = mutableListOf<StoreObserverBuilder<*>>()

  /**
   * Build final observers
   */
  fun build() = StoreObservers(*observerBuilders.map { it.build() }.toTypedArray())

  @Suppress("UNCHECKED_CAST")
  inline fun <reified T : State> observe(): DefaultStoreObserverBuilder<T,T> {
    val builder = DefaultStoreObserverBuilder(T::class, T::class, { state: T -> state })
    observerBuilders += builder
    return builder
  }

  /**
   * Add an observer
   */
  @Suppress("UNCHECKED_CAST")
  inline fun <reified T : State, reified R> observe(noinline selector: StateSelector<T, R>): DefaultStoreObserverBuilder<T,R> {
    val builder = DefaultStoreObserverBuilder(R::class, T::class, selector)
    observerBuilders += builder
    return builder
  }

  @Suppress("UNCHECKED_CAST")
  inline fun <reified R> observeComplex(noinline selector: ComplexStateSelector<R>): ComplexStoreObserverBuilder<R> {
    val builder = ComplexStoreObserverBuilder(R::class,selector)
    observerBuilders += builder
    return builder
  }


}


/**
 * Observer builder
 */
class DefaultStoreObserverBuilder<T : State, R>(
  val returnClazz: KClass<*>,
  val stateClazz: KClass<T>,
  val selector: StateSelector<T, R>,
  private var updater: StoreUpdateHandler<R>? = null
) : StoreObserverBuilder<R> {



  infix fun update(updater: StoreUpdateHandler<R>) {
    this.updater = updater
  }

  @Suppress("UNCHECKED_CAST")
  override fun build(): StoreObserver<R> {
    requireNotNull(updater) { "You must provide an updater" }
    return DefaultStoreObserver(
      StoreContext.store,
      stateClazz,
      returnClazz,
      selector as StoreSelector<Any, Any>,
      updater!! as StoreUpdateHandler<Any>
    ) as StoreObserver<R>
  }
}

interface StoreObserverBuilder<R> {
  fun build(): StoreObserver<R>
}

/**
 * Complex store observer
 */
class ComplexStoreObserverBuilder<R>(
  val returnClazz: KClass<*>,
  val selector: ComplexStateSelector<R>
) : StoreObserverBuilder<R> {

  /**
   * The updater
   */
  private var updater: StoreUpdateHandler<R>? = null

  /**
   * Set the updater
   */
  infix fun update(updater: StoreUpdateHandler<R>) {
    this.updater = updater
  }

  override fun build(): StoreObserver<R> {
    requireNotNull(updater, { "You must provide an updater" })
    return ComplexStoreObserver(
      StoreContext.store,
      selector,
      updater!!
    )
  }
}

/**
 * Rapidly add observers
 */
fun observations(body: StoreObserversBuilder.() -> Unit): StoreObservers {
  val builder = StoreObserversBuilder()
  builder.body()
  return builder.build()
}


/**
 * Simple value selector
 */
inline fun <reified T : State, reified R> value(selector:StateSelector<T,R>):R {
  val store = StoreContext.store
  val state = store.getLeafState(T::class)
  return selector(state)
}

/**
 * Complex value selector
 */
inline fun <reified R> valueComplex(selector:ComplexStateSelector<R>):R {
  val store = StoreContext.store
  val context = ComplexStateSelectorContext(store,null)

  return context.selector()
}