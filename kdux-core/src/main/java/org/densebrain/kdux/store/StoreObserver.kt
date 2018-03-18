package org.densebrain.kdux.store

import org.slf4j.LoggerFactory
import java.io.Closeable
import kotlin.reflect.KClass

typealias StoreUpdateHandler<T,R> = (newValue:R, oldValue:R?, state:T) -> Unit

typealias StoreSelector<T,R> = (state:T) -> R

class StoreObserver<T: State,R>  (
  private val store: Store,
  private val stateKlazz:KClass<T>,
  private val returnKlazz:KClass<*>,
  private val getter: StoreSelector<T,R>,
  private val updater: StoreUpdateHandler<T,R>
) : Closeable {

  companion object {
    private val log = LoggerFactory.getLogger(StoreObserver::class.java)
  }

  /**
   * Last value cached for performance
   */
  private var lastValue:R? = null


  /**
   * Run the observer's updater function
   */
  @Suppress("UNCHECKED_CAST")
  fun run(rootState: RootState) {
    val leafState = rootState[stateKlazz] as? T ?: run {
      log.warn("Leaf state is null ${stateKlazz.java.name}")
      return
    }

    val newValue = getter(leafState)
    if (newValue !== lastValue) {
      val lastValue = this.lastValue
      this.lastValue = newValue

      if (isAttached)
        updater(newValue,lastValue,leafState)
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
  fun forceUpdate(): StoreObserver<T,R> {
    lastValue = null
    run(store.getRootState())
    return this
  }

  /**
   * If not currently attached then attach to the store
   */
  fun attach(doUpdate:Boolean = true): StoreObserver<T,R> {
    store.addObserver(this)

    return if (doUpdate) forceUpdate() else this
  }

  /**
   * Detach - synonym to close
   */
  fun detach(): StoreObserver<T,R> {
    store.removeObserver(this)
    return this
  }

  /**
   * Synonym to detach
   */
  override fun close() {
    detach()
  }

  init {

  }

}