package org.densebrain.kdux.android

import org.densebrain.kdux.store.Store
import android.content.Context
import kotlin.reflect.KClass

class AndroidStore(context: Context, vararg actionClazzes: KClass<*>) : Store(*actionClazzes) {

  init {
    updateScheduler = AndroidStoreUpdateScheduler(context)
  }
}