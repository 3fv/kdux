package org.densebrain.kdux.example.store

import org.densebrain.kdux.store.State

data class ExampleState(
  val count:Int = 0,
  val count2:Int = 10
) : State