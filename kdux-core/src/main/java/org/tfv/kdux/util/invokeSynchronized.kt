package org.tfv.kdux.util

inline fun <T> invokeSynchronized(lock: Any, crossinline body: () -> T):T {
  synchronized(lock) {
    return body()
  }
}
