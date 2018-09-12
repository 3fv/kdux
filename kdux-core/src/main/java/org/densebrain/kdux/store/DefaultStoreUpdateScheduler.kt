package org.densebrain.kdux.store

import arrow.effects.DeferredK

class DefaultStoreUpdateScheduler : StoreUpdateScheduler {

  override fun schedule(update: StoreUpdate) {
    DeferredK {
      update.run()
    }
  }
}