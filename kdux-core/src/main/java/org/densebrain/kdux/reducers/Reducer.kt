package org.densebrain.kdux.reducers

import org.densebrain.kdux.actions.Actions
import org.densebrain.kdux.store.StoreContext


typealias ReducerType<T>  = (state:T) -> T
interface Reducer<T> {
  fun handle(state:T):T
}


fun <T> reducer(actions: Actions<*>, body: ReducerType<T>) =StoreContext.store.dispatch(actions, object : Reducer<T> {
  override fun handle(state: T): T {
    return body.invoke(state)
  }
})



/**
 * No call reducer - place holder for returned actions
 */
@Suppress("UNUSED_PARAMETER")
class NoCallReducer<T>(reducer: Reducer<T>): Reducer<T> {
  override fun handle(state: T): T {
    throw IllegalAccessError("You should never call a reducer directly returned from Actions")
  }
}