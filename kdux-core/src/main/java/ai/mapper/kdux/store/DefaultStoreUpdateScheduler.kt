package ai.mapper.kdux.store

import arrow.effects.DeferredKW

class DefaultStoreUpdateScheduler : StoreUpdateScheduler {

  override fun schedule(update: StoreUpdate) {
    DeferredKW {
      update.run()
    }
  }
}