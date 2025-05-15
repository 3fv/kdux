package org.tfv.kdux.util

import kotlin.reflect.KClass

object LogFactory {
  fun get(category: String) = Logger(category)
  fun get(clazz: KClass<*>) = Logger(clazz.simpleName!!)
}