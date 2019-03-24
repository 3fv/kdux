package org.densebrain.kdux.util

import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals

class ReflectionTest {

  @Test
  fun testObjectInstance() {
    js("""
       function TestClass() {
         this.prop = 123
      }
    """)
    val clazz = js("TestClass").unsafeCast<KClass<*>>()
    val instance:dynamic = Reflection.objectInstance(clazz)
    val value = instance.prop.unsafeCast<Double>()
    assertEquals(value,123.0)
  }

}