package org.densebrain.kdux.example

import org.densebrain.kdux.android.configureAndroid
import org.densebrain.kdux.example.store.ExampleActions
import org.densebrain.kdux.example.store.ExampleActions2
import org.densebrain.kdux.store.StoreContext


class Application : android.app.Application() {

  override fun onCreate() {
    super.onCreate()
//    val startFlipper= {
//      if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
//        SoLoader.init(this, false)
//        AndroidFlipperClient.Builder(this)
//          .withPlugins(
//            InspectorFlipperPlugin(this@Application, DescriptorMapping.withDefaults()),
//            KduxFlipperPlugin()
//          )
//          .start()
//
//
//      }
//    }

//    StoreContext.events.ready.on {
//
//    }

    StoreContext.configureAndroid(this, ExampleActions::class, ExampleActions2::class)
//    startFlipper()
  }
}