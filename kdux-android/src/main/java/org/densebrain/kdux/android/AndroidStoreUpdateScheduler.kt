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

class AndroidStoreUpdateScheduler(context: Context) : StoreUpdateScheduler, LifecycleObserver {

  private var updateThread:HandlerThread? = null
  private var updateHandler:Handler? = null

  override fun schedule(update: StoreUpdate) {
    updateHandler?.post(update)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  fun onStart() {
    updateThread = HandlerThread(AndroidStoreUpdateScheduler::class.java.name).apply {
      start()
    }
    updateHandler = Handler(updateThread!!.looper)

  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  fun onStop() {
    updateThread?.quitSafely()
  }

  init {
    ProcessLifecycleOwner.get().lifecycle.addObserver(this)
  }




}