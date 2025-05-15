package org.tfv.kdux.store

//import org.slf4j.LoggerFactory
//import java.io.Closeable
import org.tfv.kdux.util.LogFactory
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
open class DefaultStoreObserver<T : State, R>(
  protected val store: Store,
  protected val stateKlazz: KClass<T>,
  protected val returnKlazz: KClass<*>,
  protected val getter: StoreSelector<T, R>,
  protected val updater: StoreUpdateHandler<R>
) : StoreObserver<R> {

  companion object {
    private val log = LogFactory.get(StoreObserver::class)
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
    val leafState = when(RootState::class) {
      stateKlazz -> rootState
      else ->rootState[stateKlazz] as? T ?: run {
        log.warn("Leaf state is null ${stateKlazz.simpleName}")
        return
      }
    }
    val newValue = getter(leafState as T)
    if (newValue !== lastValue) {
      val oldValue = this.lastValue
      this.lastValue = newValue

      if (isAttached)
        updater(newValue, oldValue)
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
  fun close() {
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