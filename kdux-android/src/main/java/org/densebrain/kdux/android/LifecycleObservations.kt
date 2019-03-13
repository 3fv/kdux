package org.densebrain.kdux.android

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import org.densebrain.kdux.store.StoreObservers

class LifecycleObservations(
  private val observations: StoreObservers,
  private val updateOnAttach: Boolean = true
) : LifecycleObserver {

  init {
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
