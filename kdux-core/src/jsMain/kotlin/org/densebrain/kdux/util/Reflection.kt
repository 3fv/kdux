package org.densebrain.kdux.util

import kotlin.reflect.KClass

actual object Reflection {
  actual fun <T : Any> objectInstance(clazz: KClass<T>): T = js("new clazz()") as T
}