package org.densebrain.kdux.processor

import com.squareup.kotlinpoet.ClassName

fun String.asClassName():ClassName {

  val parts = this.split(".")
  if (parts.isNotEmpty()) {
    val clazzName = parts.lastOrNull()!!
    val packageName = parts.dropLast(1).joinToString(".")
    return ClassName(packageName, clazzName)
  }

  return ClassName("", this)
}