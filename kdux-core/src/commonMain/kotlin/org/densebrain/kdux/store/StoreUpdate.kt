package org.densebrain.kdux.store

expect interface StoreUpdate {
  val store:Store

  fun run()
}