package ai.mapper.kdux.example.store

import ai.mapper.kdux.actions.AbstractActions
import ai.mapper.kdux.reducers.Reducer
import ai.mapper.kdux.annotations.ActionReducer
import ai.mapper.kdux.annotations.ActionsBuilder

/**
 * Sample action builder
 */
@ActionsBuilder
abstract class ExampleActions2 : AbstractActions<ExampleState2>() {

  /**
   * Return the state type
   */
  override val stateType
    get() = ExampleState2::class


  /**
   * Short hand reducer
   */
  @ActionReducer
  open fun incrementCounter(increment:Int) = reducer { it.copy(count = it.count + increment) }

  /**
   * Medium
   */
  @ActionReducer
  open fun multiplyCounter(multiplier:Int): Reducer<ExampleState2> {
    return reducer { it.copy(count = it.count * multiplier) }
  }

  /**
   * Long
   */
  @ActionReducer
  open fun divideCounter(divisor:Int): Reducer<ExampleState2> {
    return object : Reducer<ExampleState2> {
      override fun handle(state: ExampleState2): ExampleState2 {
        return state.copy(count = state.count / divisor)
      }
    }
  }
}