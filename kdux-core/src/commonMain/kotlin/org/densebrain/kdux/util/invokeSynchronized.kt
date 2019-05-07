package org.densebrain.kdux.util

expect inline fun <T> invokeSynchronized(lock:Any, crossinline body:() -> T):T