package org.densebrain.kdux.util

import kotlin.reflect.KClass
//import kotlin.reflect.full.memberProperties

expect fun <T : Any> shallowEquals(clazz: KClass<T>, src:T, target:T):Boolean

