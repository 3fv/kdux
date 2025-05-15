package org.tfv.kdux.store

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DefaultStoreUpdateScheduler : StoreUpdateScheduler {

  override fun schedule(update: StoreUpdate) {
    GlobalScope.launch {
      update.run()
    }
  }
}