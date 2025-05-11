package org.densebrain.kdux.util

import kotlin.reflect.KClass

object Reflection {
  fun <T : Any> objectInstance(clazz: KClass<T>): T =
    clazz.java.newInstance() as T

}