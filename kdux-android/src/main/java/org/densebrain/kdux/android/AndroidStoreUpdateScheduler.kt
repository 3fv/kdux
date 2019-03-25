package org.densebrain.kdux.android

import org.densebrain.kdux.store.StoreUpdate
import org.densebrain.kdux.store.StoreUpdateScheduler
import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import java.lang.IllegalStateException

class AndroidStoreUpdateScheduler(context: Context) : StoreUpdateScheduler, LifecycleObserver {

  private var updateThread:HandlerThread? = null
  private var updateHandler:Handler? = null

  @Synchronized
  private fun startHandlerThread(force:Boolean = false) {
    if (updateHandler == null || updateThread == null || !updateThread!!.isAlive || force) {
      if (updateThread != null) {
        try {
          updateThread!!.quitSafely()
        } catch (ex:Exception) {}
      }
      updateThread = HandlerThread(AndroidStoreUpdateScheduler::class.java.name).apply {
        start()
      }

      updateHandler = Handler(updateThread!!.looper)
    }
  }


  @Synchronized
  override fun schedule(update: StoreUpdate) {
    try {
      startHandlerThread()
      updateHandler?.post(update)
    } catch (ex:IllegalStateException) {
      schedule(update)
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  fun onStart() {
    startHandlerThread()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  fun onStop() {
    updateThread?.quitSafely()
  }

  init {
    ProcessLifecycleOwner.get().lifecycle.addObserver(this)
  }




}