package org.densebrain.kdux.util

inline fun <T> invokeSynchronized(lock: Any, crossinline body: () -> T):T {
  synchronized(lock) {
    return body()
  }
}
