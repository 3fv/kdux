package org.densebrain.kdux.android

import androidx.lifecycle.LiveData
import org.densebrain.kdux.store.State
import org.densebrain.kdux.store.StateSelector
import org.densebrain.kdux.store.observations
import kotlin.reflect.KClass

open class StateLiveData<S : State>(
  private val stateClazz:KClass<S>
) : LiveData<S>() {

  private val observers = observations {
    observerBuilders += DefaultStoreObserverBuilder(
      stateClazz,
      stateClazz,
      {state -> state},
      {newValue, _ -> postValue(newValue)}
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