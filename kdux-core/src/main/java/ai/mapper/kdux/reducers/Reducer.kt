package ai.mapper.kdux.reducers


typealias ReducerType<T>  = (state:T) -> T
interface Reducer<T> {
  fun handle(state:T):T
}


fun <T> reducer(body: ReducerType<T>): Reducer<T> {
  return object : Reducer<T> {
    override fun handle(state: T): T {
      return body.invoke(state)
    }
  }
}


/**
 * No call reducer - place holder for returned actions
 */
@Suppress("UNUSED_PARAMETER")
class NoCallReducer<T>(reducer: Reducer<T>): Reducer<T> {
  override fun handle(state: T): T {
    throw IllegalAccessError("You should never call a reducer directly returned from Actions")
  }
}