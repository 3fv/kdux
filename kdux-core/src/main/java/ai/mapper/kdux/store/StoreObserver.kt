package ai.mapper.kdux.store

import org.slf4j.LoggerFactory
import java.io.Closeable
import kotlin.reflect.KClass

typealias StoreObserverUpdater<R,T> = (newValue:R,state:T,store: Store) -> Unit

typealias StoreObserverGetter<R,T> = (state:T) -> R

class StoreObserver<R,T: State>  (
  private val store: Store,
  private val stateKlazz:KClass<T>,
  private val returnKlazz:KClass<*>,
  private val getter: StoreObserverGetter<R, T>,
  private val updater: StoreObserverUpdater<R, T>
) : Closeable {

  companion object {
    private val log = LoggerFactory.getLogger(StoreObserver::class.java)
  }

  private var lastValue:R? = null

  /**
   * Run the observer's updater function
   */
  fun run(rootState: RootState) {
    @Suppress("UNCHECKED_CAST")
    val leafState = rootState[stateKlazz] as? T ?: run {
      log.warn("Leaf state is null ${stateKlazz.java.name}")
      return
    }

    val newValue = getter(leafState)
    if (newValue !== lastValue) {
      lastValue = newValue

      if (isAttached)
        updater(newValue,leafState,store)
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
  fun forceUpdate(): StoreObserver<R, T> {
    lastValue = null
    run(store.getRootState())
    return this
  }

  /**
   * If not currently attached then attach to the store
   */
  fun attach(doUpdate:Boolean = true): StoreObserver<R, T> {
    store.addObserver(this)

    return if (doUpdate) forceUpdate() else this
  }

  /**
   * Detach - synonym to close
   */
  fun detach(): StoreObserver<R, T> {
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