package org.densebrain.kdux.store

interface StoreUpdate {
  val store:Store

  fun run()
}