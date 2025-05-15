package org.tfv.kdux.properties

import org.tfv.kdux.store.State
import org.tfv.kdux.store.StateSelector
import org.tfv.kdux.store.StoreContext
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * State Property
 */
class StateProperty<T:State, out R>(
  private val stateClazz:KClass<T>,
  private val returnClazz:KClass<*>,
  private val selector:StateSelector<T,R>
) {
  operator fun getValue(thisRef: Any?, prop: KProperty<*>) =
    selector(StoreContext.store.getLeafState(stateClazz))
}

/**
 * Short hand to create a property
 */
inline fun <reified T:State, reified R> stateProperty(noinline selector:StateSelector<T,R>) =
  StateProperty(T::class,R::class,selector)