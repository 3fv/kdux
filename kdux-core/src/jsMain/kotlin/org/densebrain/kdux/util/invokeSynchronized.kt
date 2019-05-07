package org.densebrain.kdux.util

actual inline fun  <T> invokeSynchronized(lock: Any, crossinline body: () -> T) =
  body()
