package org.densebrain.kdux.android

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import arrow.core.Try
import arrow.core.recover
import org.densebrain.kdux.store.*
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.min
import kotlin.reflect.KClass

open class AndroidStoreObserver<T : State, R>(
  store: Store,
  stateKlazz: KClass<T>,
  returnKlazz: KClass<*>,
  getter: StoreSelector<T, R>,
  updater: StoreUpdateHandler<R>,
  protected val debounceOptions: DebounceOptions
) : DefaultStoreObserver<T, R>(
  store,
  stateKlazz,
  returnKlazz,
  getter,
  updater
) {


  /**
   * The companion object is where the magic
   * happens on this one for debouncing etc
   */
  companion object : LifecycleObserver {

    private val TAG = AndroidStoreObserver::class.java.name

    private var debounceThread:HandlerThread? = null



    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
      debounceThread = HandlerThread(AndroidStoreObserver::class.java.name).apply {
        start()
      }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
      debounceThread?.quitSafely()
    }

    init {
      ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }
  }

  private val androidStore
    get() = store as AndroidStore

  private var debounceHandler:Handler? = null

  /**
   * Observer id for tracking
   */
  private val id = UUID.randomUUID().toString()

  /**
   * Last time the function was invoked
   */
  private val invokedAt = AtomicLong(0L)

  /**
   * Is this the very first dispatch
   */
  private val firstDispatch = AtomicBoolean(true)

  /**
   * Push the update through the main dispatcher
   * on the android store
   */
  private fun pushUpdate(update: Update) {
    invokedAt.set(System.currentTimeMillis())
    androidStore.pushUpdate(ImmediateUpdate(this,update.rootState))
  }

  /**
   * Run the actual observer
   */
  private fun runInternal(rootState: RootState) {
    super.run(rootState)
  }

  /**
   * Run the observer,
   * no debounce - immediate dispatch
   * debounce - schedule
   */
  override fun run(rootState: RootState) {
    if (debounceOptions.duration < 1L || (firstDispatch.getAndSet(false) && debounceOptions.immediateFirst)) {
      pushUpdate(ImmediateUpdate(this, rootState))
    } else {
      val requestedAt = System.currentTimeMillis()
      if (invokedAt.get() == 0L) {
        invokedAt.set(requestedAt)
      }
      scheduleUpdate(DebounceUpdate(this,rootState, invokedAt.get(), requestedAt))
    }
  }

  @Synchronized
  private fun scheduleUpdate(update:DebounceUpdate) {
    Try {
      if (debounceHandler == null) {
        val looper = debounceThread?.looper ?: return
        debounceHandler = Handler(looper)
      }

      try {
        debounceHandler!!.removeCallbacksAndMessages(null)
        debounceHandler!!.postDelayed(update, update.scheduleDelay)
      } catch (ex:IllegalStateException) {

        // DESTROY OLD DEBOUNCE THREAD
        Try { debounceThread!!.quitSafely() }

        // NEW THREAD & HANDLER
        debounceThread = HandlerThread(AndroidStoreObserver::class.java.name).apply {
          start()
        }
        debounceHandler = null

        scheduleUpdate(update)
      }
    }.recover { ex -> Log.e(TAG,"Unable to schedule update", ex) }
  }

  private interface Update : Runnable {
    val rootState: RootState
  }

  private data class ImmediateUpdate(val observer: AndroidStoreObserver<*,*>, override val rootState: RootState) : Update {
    override fun run() {
      observer.runInternal(rootState)
    }
  }
  /**
   * The actual update
   */
  private data class DebounceUpdate(val observer: AndroidStoreObserver<*,*>, override val rootState: RootState, val invokedAt: Long, val requestedAt: Long) : Update {

    val id
      get() = observer.id

    val scheduleDelay:Long

    init {
      val options = observer.debounceOptions
      val duration = options.duration
      val maxWait = options.maxWait

      var scheduledTime = requestedAt + duration
      if (maxWait > 0L) {
        scheduledTime = min(invokedAt + maxWait, scheduledTime)
      }

      scheduleDelay = scheduledTime - requestedAt
    }

    override fun run() {
      observer.pushUpdate(this)
    }

    override fun equals(o: Any?): Boolean {
      val other = o as? DebounceUpdate ?: return false
      return other.id == id
    }

    override fun hashCode(): Int {
      return id.hashCode()
    }
  }

  /**
   * Debounce options
   */
  data class DebounceOptions(val duration: Long = -1L, val maxWait: Long = -1L, val immediateFirst: Boolean = true)



}