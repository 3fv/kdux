package org.densebrain.kdux.android

import org.densebrain.kdux.store.StoreContext
import android.content.Context
import kotlin.reflect.KClass

/**
 * Android configure
 */
fun StoreContext.configureAndroid(context: Context, vararg actionClazzes: KClass<*>) = configure(AndroidStore(context, *actionClazzes))

