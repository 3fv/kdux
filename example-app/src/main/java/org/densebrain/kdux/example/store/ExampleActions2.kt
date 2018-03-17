package org.densebrain.kdux.example.store

import org.densebrain.kdux.actions.AbstractActions
import org.densebrain.kdux.reducers.Reducer

/**
 * Sample action builder
 */

class ExampleActions2 : AbstractActions<ExampleState2>() {

  /**
   * Return the state type
   */
  override val stateType
    get() = ExampleState2::class


  /**
   * Short hand reducer
   */
  fun incrementCounter(increment:Int) = reducer { it.copy(count = it.count + increment) }

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
    getStore().dispatch(this,object : Reducer<ExampleState2> {
      override fun handle(state: ExampleState2): ExampleState2 {
        return state.copy(count = state.count / divisor)
      }
    })
  }
}