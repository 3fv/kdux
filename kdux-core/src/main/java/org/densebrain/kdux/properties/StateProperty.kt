package org.densebrain.kdux.properties

import org.densebrain.kdux.store.State
import org.densebrain.kdux.store.StateSelector
import org.densebrain.kdux.store.StoreContext
import kotlin.reflect.KClass

/**
 * State Property
 */
class StateProperty<T:State, out R>(
  private val stateClazz:KClass<T>,
  private val returnClazz:KClass<*>,
  private val selector:StateSelector<R,T>
) {
  val value: R
    get() = selector(StoreContext.store.getLeafState(stateClazz))
}

/**
 * Short hand to create a property
 */
inline fun <reified T:State, reified R> stateProperty(noinline selector:StateSelector<R,T>) =
  StateProperty(T::class,R::class,selector)