package org.tfv.kdux.example.store

import org.tfv.kdux.store.State

data class ExampleState(
  val count:Int = 0,
  val count2:Int = 10
) : State