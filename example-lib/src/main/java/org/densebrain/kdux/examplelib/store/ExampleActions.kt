package org.densebrain.kdux.examplelib.store

import org.densebrain.kdux.actions.AbstractActions
import kotlin.reflect.KClass


class ExampleActions : AbstractActions<ExampleState>() {

  override val stateType: KClass<ExampleState>
    get() = ExampleState::class


  fun doStuff(count:Int) = reducer { state ->
    state.copy(count = count)
  }

}