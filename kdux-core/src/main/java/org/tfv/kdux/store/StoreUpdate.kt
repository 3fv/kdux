package org.tfv.kdux.store

interface StoreUpdate : Runnable {
  val store: Store

  override fun run()
}