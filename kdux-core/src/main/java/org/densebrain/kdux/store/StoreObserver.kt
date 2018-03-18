package org.densebrain.kdux.store

import org.slf4j.LoggerFactory
import java.io.Closeable
import kotlin.reflect.KClass

typealias StoreUpdateHandler<R> = (newValue: R, oldValue: R?) -> Unit

typealias StoreSelector<T, R> = (state: T) -> R

/**
 * Store observer shape
 */
interface StoreObserver<R> {
  fun run(rootState: RootState)
  fun attach(doUpdate: Boolean = true):StoreObserver<R>
  fun detach():StoreObserver<R>
}

/**
 * Default (Simple) observer
 */
class DefaultStoreObserver<T : State, R>(
  private val store: Store,
  private val stateKlazz: KClass<T>,
  private val returnKlazz: KClass<*>,
  private val getter: StoreSelector<T, R>,
  private val updater: StoreUpdateHandler<R>
) : Closeable, StoreObserver<R> {

  companion object {
    private val log = LoggerFactory.getLogger(StoreObserver::class.java)
  }

  /**
   * Last value cached for performance
   */
  private var lastValue: R? = null


  /**
   * Run the observer's updater function
   */
  @Suppress("UNCHECKED_CAST")
  override fun run(rootState: RootState) {
    val leafState = rootState[stateKlazz] as? T ?: run {
      log.warn("Leaf state is null ${stateKlazz.java.name}")
      return
    }

    val newValue = getter(leafState)
    if (newValue !== lastValue) {
      val lastValue = this.lastValue
      this.lastValue = newValue

      if (isAttached)
        updater(newValue, lastValue)
    }
  }

  /**
   * Is attached
   */
  val isAttached
    get() = store.isObserverAttached(this)

  /**
   * Clear last value and force update (not on scheduler)
   */
  fun forceUpdate(): StoreObserver<R> {
    lastValue = null
    run(store.getRootState())
    return this
  }

  /**
   * If not currently attached then attach to the store
   */
  override fun attach(doUpdate: Boolean): StoreObserver<R> {
    store.addObserver(this)

    return if (doUpdate) forceUpdate() else this
  }

  /**
   * Detach - synonym to close
   */
  override fun detach(): StoreObserver<R> {
    store.removeObserver(this)
    return this
  }

  /**
   * Synonym to detach
   */
  override fun close() {
    detach()
  }

}

/**
 * Complex State Selector
 *
 * support multi state, etc
 */
typealias ComplexStateSelector<R> = ComplexStateSelectorContext<R>.() -> R

/**
 * Complex Store observer for complex state selector
 */
class ComplexStoreObserver<R>(
  private val store: Store,
  private val selector: ComplexStateSelector<R>,
  private val updater: StoreUpdateHandler<R>
) : StoreObserver<R>{

  private var lastValue:R? = null

  override fun run(rootState: RootState) {
    val context = ComplexStateSelectorContext(store,lastValue)
    val value = context.selector()
    if (value !== lastValue) {
      val lastValue = this.lastValue
      this.lastValue = value
      updater.invoke(value,lastValue)
    }
  }

  override fun attach(doUpdate: Boolean): StoreObserver<R> {
    store.addObserver(this)
    return this
  }

  override fun detach(): StoreObserver<R> {
    store.removeObserver(this)
    return this
  }
}

class ComplexStateSelectorContext<out R>(val store: Store, val lastValue:R?) {
  inline fun <reified T: State> get() : T = store.getLeafState(T::class)
}