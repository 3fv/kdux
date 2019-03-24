package org.densebrain.kdux.util

import org.slf4j.LoggerFactory

actual class Logger actual constructor(category: String) {

  private val log = LoggerFactory.getLogger(category)

  actual fun debug(msg: String, ex: Exception?) = log.debug(msg,ex)

  actual fun info(msg: String, ex: Exception?) = log.info(msg,ex)

  actual fun warn(msg: String, ex: Exception?) = log.warn(msg,ex)

  actual fun error(msg: String, ex: Exception?) = log.warn(msg,ex)

}