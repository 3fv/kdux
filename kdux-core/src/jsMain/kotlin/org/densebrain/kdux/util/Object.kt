package org.densebrain.kdux.util

object Object {
  fun entries(json: dynamic) = js("Object").values(json).unsafeCast<Array<Array<Any>>>().map {
    Pair(it[0].unsafeCast<String>(),it[1].unsafeCast<Any>())
  }
  fun values(json: dynamic) = js("Object").values(json).unsafeCast<Array<Any>>()
  fun keys(json: dynamic) = js("Object").keys(json).unsafeCast<Array<String>>()
}