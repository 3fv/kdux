package org.densebrain.kdux.android

import org.densebrain.kdux.store.Store
import android.content.Context
import android.os.Handler
import android.os.Looper
import org.densebrain.kdux.store.RootState
import org.densebrain.kdux.store.StoreObserver
import org.densebrain.kdux.store.StoreUpdate
import kotlin.reflect.KClass



class AndroidStore(context: Context, looper: Looper, vararg actionClazzes: KClass<*>) : Store(*actionClazzes) {

  private val pushHandler = Handler(looper)

  init {
    updateScheduler = AndroidStoreUpdateScheduler(context)
  }

  fun pushUpdate(update: StoreUpdate) {
    pushUpdate(StoreUpdateRunner(update))
  }

  fun pushUpdate(updateRunnable: Runnable) {
    pushHandler.post(updateRunnable)
  }

  override fun update(rootState: RootState) {
    lateinit var observers: Set<StoreObserver<*>>
    synchronized(this.observers) {
      observers = this.observers.toSet()
    }

    observers.forEach {
      when (it) {
        is AndroidStoreObserver<*,*> -> it.run(rootState)
        else -> pushUpdate(ObserverUpdateRunner(it,rootState))
      }
    }
  }

  private class ObserverUpdateRunner(val observer: StoreObserver<*>, val rootState: RootState) : Runnable {
    override fun run() {
      observer.run(rootState)
    }
  }

  private class StoreUpdateRunner(val update: StoreUpdate) : Runnable {
    override fun run() {
      update.run()
    }
  }



}