package org.densebrain.kdux.examplelib.store

import org.densebrain.kdux.actions.AbstractActions
import org.densebrain.kdux.annotations.ActionReducer
import org.densebrain.kdux.annotations.ActionsBuilder

import kotlin.reflect.KClass


@ActionsBuilder
abstract class ExampleActions : AbstractActions<ExampleState>() {

  override val stateType: KClass<ExampleState>
    get() = ExampleState::class


  @ActionReducer
  open fun doStuff(count:Int) = reducer { state ->
    state
  }

}