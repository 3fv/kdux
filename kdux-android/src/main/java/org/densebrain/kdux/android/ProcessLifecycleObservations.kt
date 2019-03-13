package org.densebrain.kdux.android

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import org.densebrain.kdux.store.StoreObservers

class ProcessLifecycleObservations(
  private val observations: StoreObservers,
  private val updateOnAttach: Boolean = true,
  manualBind: Boolean = false
) : LifecycleObserver {

  init {
    if (!manualBind)
      bind()
  }

  fun bind() {
    ProcessLifecycleOwner.get().lifecycle.addObserver(this)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  fun onStart() {
    observations.attach(updateOnAttach)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  fun onStop() {
    observations.detach()
  }
}
