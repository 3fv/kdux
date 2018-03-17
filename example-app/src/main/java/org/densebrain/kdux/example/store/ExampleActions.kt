package org.densebrain.kdux.example.store

import org.densebrain.kdux.actions.AbstractActions
import org.densebrain.kdux.reducers.Reducer

/**
 * Sample action builder
 */

class ExampleActions : AbstractActions<ExampleState>() {

  /**
   * Return the state type
   */
  override val stateType
    get() = ExampleState::class


  fun addSumToCounter(values:List<Int?>) = reducer { it.copy(count = it.count + values.filterNotNull().sum()) }

  /**
   * Short hand reducer
   */
  fun incrementCounter(increment:Int) = reducer { it.copy(count = it.count + increment) }

  /**
   * Counter 2
   */
  fun incrementCounter2(increment:Int) = reducer { it.copy(count2 = it.count2 + increment) }

  /**
   * Medium
   */
  
  fun multiplyCounter(multiplier:Int) {
    reducer { it.copy(count = it.count * multiplier) }
  }

  /**
   * Long
   */
  fun divideCounter(divisor:Int) {
    getStore().dispatch(this,object : Reducer<ExampleState> {
      override fun handle(state: ExampleState): ExampleState {
        return state.copy(count = state.count / divisor)
      }
    })
  }
}