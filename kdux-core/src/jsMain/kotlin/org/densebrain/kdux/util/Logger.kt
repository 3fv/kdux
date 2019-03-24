package org.densebrain.kdux.util

actual class Logger actual constructor(category: String) {

  actual fun debug(msg: String, ex: Exception?) = console.log(msg,ex)

  actual fun info(msg: String, ex: Exception?) = console.info(msg,ex)

  actual fun warn(msg: String, ex: Exception?) = console.warn(msg,ex)

  actual fun error(msg: String, ex: Exception?) = console.error(msg,ex)
}