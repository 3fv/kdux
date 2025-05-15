package org.tfv.kdux.android

import android.content.Context
import android.os.Looper
import org.tfv.kdux.store.*
import kotlin.reflect.KClass

/**
 * Android configure
 */
fun StoreContext.configureAndroid(context: Context, vararg actionClazzes: KClass<*>) =
  configureAndroidWithLooper(context,context.mainLooper,*actionClazzes)

fun StoreContext.configureAndroidWithLooper(context: Context, looper: Looper, vararg actionClazzes: KClass<*>) =
  configure(AndroidStore(context, looper, *actionClazzes))


open class AndroidStoreObserversBuilder  {

  /**
   * Keep all builders handy
   */
  val observerBuilders = mutableListOf<StoreObserverBuilder<*>>()

  /**
   * Build final observers
   */
  fun build() = StoreObservers(*observerBuilders.map { it.build() }.toTypedArray())

  @Suppress("UNCHECKED_CAST")
  inline fun <reified T : State> observe(): AndroidStoreObserverBuilder<T,T> {
    val builder = AndroidStoreObserverBuilder(T::class, T::class, { state: T -> state })
    observerBuilders += builder
    return builder
  }

  /**
   * Add an observer
   */
  @Suppress("UNCHECKED_CAST")
  inline fun <reified T : State, reified R> observe(noinline selector: StateSelector<T, R>): AndroidStoreObserverBuilder<T,R> {
    val builder = AndroidStoreObserverBuilder(R::class, T::class, selector)
    observerBuilders += builder
    return builder
  }



}


/**
 * Observer builder
 */
class AndroidStoreObserverBuilder<T : State, R>(
  val returnClazz: KClass<*>,
  val stateClazz: KClass<T>,
  val selector: StateSelector<T, R>,
  private var updater: StoreUpdateHandler<R>? = null,
  private var debounceOptions:AndroidStoreObserver.DebounceOptions = AndroidStoreObserver.DebounceOptions()
) : StoreObserverBuilder<R> {





  /**
   * Set debounce without a max wait
   */
  infix fun debounce(duration:Long): AndroidStoreObserverBuilder<T,R> {
    this.debounceOptions = AndroidStoreObserver.DebounceOptions(duration)
    return this
  }

  /**
   * Set debounce options
   */
  infix fun debounce(options: AndroidStoreObserver.DebounceOptions): AndroidStoreObserverBuilder<T,R> {
    this.debounceOptions = options
    return this
  }

  infix fun update(updater: StoreUpdateHandler<R>): AndroidStoreObserverBuilder<T,R> {
    this.updater = updater
    return this
  }

  @Suppress("UNCHECKED_CAST")
  override fun build(): StoreObserver<R> {
    requireNotNull(updater) { "You must provide an updater" }
    return AndroidStoreObserver(
      StoreContext.store,
      stateClazz,
      returnClazz,
      selector as StoreSelector<Any, Any>,
      updater!! as StoreUpdateHandler<Any>,
      debounceOptions
    ) as StoreObserver<R>
  }
}

/**
 * Rapidly add observers
 */
fun androidObservations(body: AndroidStoreObserversBuilder.() -> Unit): StoreObservers {
  val builder = AndroidStoreObserversBuilder()
  builder.body()
  return builder.build()
}
