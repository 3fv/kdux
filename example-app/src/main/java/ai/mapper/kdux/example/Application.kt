package ai.mapper.kdux.example

import ai.mapper.kdux.android.configureAndroid
import ai.mapper.kdux.example.store.ExampleActions
import ai.mapper.kdux.example.store.ExampleActions2
import ai.mapper.kdux.store.StoreContext

class Application : android.app.Application() {

  override fun onCreate() {
    super.onCreate()

    StoreContext.configureAndroid(this, ExampleActions::class, ExampleActions2::class)
  }
}