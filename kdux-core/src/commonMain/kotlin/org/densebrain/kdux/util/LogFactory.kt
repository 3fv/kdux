package org.densebrain.kdux.util

import kotlin.reflect.KClass

expect class Logger(category:String) {
  fun debug(msg: String, ex:Exception? = null)
  fun info(msg: String, ex:Exception? = null)
  fun warn(msg: String, ex:Exception? = null)
  fun error(msg: String, ex:Exception? = null)
}

object LogFactory {
  fun get(category: String) = Logger(category)
  fun get(clazz: KClass<*>) = Logger(clazz.simpleName!!)
}