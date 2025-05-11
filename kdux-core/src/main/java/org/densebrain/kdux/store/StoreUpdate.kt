package org.densebrain.kdux.store

interface StoreUpdate : Runnable {
  val store: Store

  override fun run()
}