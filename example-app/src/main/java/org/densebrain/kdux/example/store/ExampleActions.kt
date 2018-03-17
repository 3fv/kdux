package org.densebrain.kdux.example.store

import org.densebrain.kdux.actions.AbstractActions
import org.densebrain.kdux.reducers.Reducer
import org.densebrain.kdux.annotations.ActionReducer
import org.densebrain.kdux.annotations.ActionsBuilder

/**
 * Sample action builder
 */
@ActionsBuilder
abstract class ExampleActions : AbstractActions<ExampleState>() {

  /**
   * Return the state type
   */
  override val stateType
    get() = ExampleState::class


  /**
   * Short hand reducer
   */
  @ActionReducer
  open fun incrementCounter(increment:Int) = reducer { it.copy(count = it.count + increment) }

  /**
   * Counter 2
   */
  @ActionReducer
  open fun incrementCounter2(increment:Int) = reducer { it.copy(count2 = it.count2 + increment) }

  /**
   * Medium
   */
  @ActionReducer
  open fun multiplyCounter(multiplier:Int): Reducer<ExampleState> {
    return reducer { it.copy(count = it.count * multiplier) }
  }

  /**
   * Long
   */
  @ActionReducer
  open fun divideCounter(divisor:Int): Reducer<ExampleState> {
    return object : Reducer<ExampleState> {
      override fun handle(state: ExampleState): ExampleState {
        return state.copy(count = state.count / divisor)
      }
    }
  }
}