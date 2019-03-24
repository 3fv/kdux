package org.densebrain.kdux.util

import kotlin.reflect.KClass

actual fun <T : Any> shallowEquals(clazz: KClass<T>, src: T, target: T): Boolean {
  val srcEntries = Object.entries(src)
  val targetEntries = Object.entries(target)

  return srcEntries.size == targetEntries.size && srcEntries.all { (srcKey,srcValue) ->
    val targetEntry = targetEntries.find { it.first == srcKey }
    targetEntry != null && targetEntry.second === srcValue
  }
}