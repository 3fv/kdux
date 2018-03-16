package ai.mapper.kdux.examplelib.store

import ai.mapper.kdux.actions.AbstractActions
import ai.mapper.kdux.annotations.ActionReducer
import ai.mapper.kdux.annotations.ActionsBuilder

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