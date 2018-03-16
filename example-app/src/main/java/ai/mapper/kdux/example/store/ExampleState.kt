package ai.mapper.kdux.example.store

import ai.mapper.kdux.store.State

data class ExampleState(
  val count:Int = 0,
  val count2:Int = 10
) : State