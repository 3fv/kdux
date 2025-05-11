package org.densebrain.kdux.util

import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
 fun <T : Any> shallowEquals(clazz: KClass<T>, src:T, target:T):Boolean {
  clazz.memberProperties.forEach { prop ->
    if (prop.get(src) !== prop.get(target))
      return false
  }

  return true
}