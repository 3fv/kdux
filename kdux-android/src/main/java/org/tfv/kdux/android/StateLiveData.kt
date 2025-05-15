package org.tfv.kdux.android

import androidx.lifecycle.LiveData
import org.tfv.kdux.store.State
import org.tfv.kdux.store.observations
import kotlin.reflect.KClass

open class StateLiveData<S : State>(
  private val stateClazz:KClass<S>,
  private val duration: Long = -1,
  private val maxWait: Long = -1
) : LiveData<S>() {

  private val observers = observations {
    observerBuilders += AndroidStoreObserverBuilder(
      stateClazz,
      stateClazz,
      {state -> state},
      {newValue, _ -> postValue(newValue)},
      AndroidStoreObserver.DebounceOptions(duration,maxWait)
    )
  }

  override fun onInactive() {
    observers.detach()
    super.onInactive()
  }

  override fun onActive() {
    observers.attach()
    super.onActive()
  }
}