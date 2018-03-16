package ai.mapper.kdux.android

import ai.mapper.kdux.store.StoreUpdate
import ai.mapper.kdux.store.StoreUpdateScheduler
import android.content.Context
import android.os.Handler

class AndroidStoreUpdateScheduler(context: Context) : StoreUpdateScheduler {

  private val mainHandler = Handler(context.mainLooper)

  override fun schedule(update: StoreUpdate) {
    mainHandler.post(StoreUpdateRunner(update))
  }

  private class StoreUpdateRunner(val update: StoreUpdate) : Runnable {
    override fun run() {
      update.run()
    }
  }
}