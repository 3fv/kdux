package org.densebrain.kdux.store

/**
 * After a dispatch, updates are scheduled
 * via implementations of this Scheduler interface
 */
interface StoreUpdateScheduler {
  /**
   * Schedule the update, in android this would likely be on the main thread
   */
  fun schedule(update: StoreUpdate)
}