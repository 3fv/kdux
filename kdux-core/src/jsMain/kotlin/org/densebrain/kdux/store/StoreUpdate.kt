package org.densebrain.kdux.store

actual interface StoreUpdate {
  actual val store: Store

  actual fun run()
}