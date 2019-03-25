package org.densebrain.kdux.store

actual interface StoreUpdate : Runnable {
  actual val store: Store

  actual override fun run()
}