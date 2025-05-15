package org.tfv.kdux.reducers

import org.tfv.kdux.actions.Actions
import org.tfv.kdux.store.StoreContext


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
    error("You should never call a reducer directly returned from Actions")
  }
}