package org.densebrain.kdux.util

import kotlin.reflect.KClass

expect object Reflection {
  fun <T : Any> objectInstance(clazz: KClass<T>):T
}