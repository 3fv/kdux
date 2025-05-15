package org.tfv.kdux.util

import org.slf4j.LoggerFactory

class Logger(category: String) {

  private val log = LoggerFactory.getLogger(category)
  fun debug(msg: String, ex: Exception?= null) = log.debug(msg, ex)
  fun info(msg: String, ex: Exception?= null) = log.info(msg, ex)
  fun warn(msg: String, ex: Exception? = null) = log.warn(msg, ex)
  fun error(msg: String, ex: Exception?= null) = log.warn(msg, ex)

}