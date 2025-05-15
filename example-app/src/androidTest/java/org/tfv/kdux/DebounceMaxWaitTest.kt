package org.tfv.kdux

import android.os.HandlerThread
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule


import org.tfv.kdux.android.AndroidStoreObserver
import org.tfv.kdux.android.androidObservations
import org.tfv.kdux.android.configureAndroidWithLooper
import org.tfv.kdux.example.MainActivity
import org.tfv.kdux.example.store.ExampleActions
import org.tfv.kdux.example.store.ExampleActions2
import org.tfv.kdux.example.store.ExampleState
import org.tfv.kdux.store.StoreContext
import org.tfv.kdux.store.actions
import org.tfv.kdux.store.state
import org.junit.*
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
@LargeTest
class DebounceMaxWaitTest {

  companion object {
    private val TAG = DebounceMaxWaitTest::class.java.name
  }

  @get:Rule
  val activityRule = ActivityTestRule(MainActivity::class.java)

  @Test
  fun ensureDebounce_10to5() {

    //val instrumentation = InstrumentationRegistry.getInstrumentation()
    val context = activityRule.activity //instrumentation.context
    val storeThread = HandlerThread(DebounceTest::class.java.name).apply {
      start()
    }

    StoreContext.configureAndroidWithLooper(
      context,
      storeThread.looper,
      ExampleActions::class,
      ExampleActions2::class
    )
    val actionCount = 10
    var updateCount = 0
    try {
      val observations = androidObservations {
        observe<ExampleState, Int> { state -> state.count } update { newCount, oldCount ->
          Log.i(TAG, "Update received: ${newCount}/${oldCount}")
          updateCount++
        } debounce AndroidStoreObserver.DebounceOptions(150L, 200L, false)
      }
      try {
        observations.attach(false)


        val actions = actions<ExampleActions>()
        try {
          0.until(actionCount).forEach { index ->
            Log.i(TAG, "Sent update ${index}")
            assertEquals("Check ${index}", index / 2, updateCount)
            actions.incrementCounter(1)
            Thread.sleep(100L)
          }
        } catch (ex: Throwable) {
          Log.e(TAG, "Check failed", ex)
        }

        Thread.sleep(1600L)
        assertEquals("Finally update count check", actionCount / 2, updateCount)
        assertEquals("State validation check", 10, state<ExampleState>().count)

        Log.i(TAG, "Final value: ${state<ExampleState>().count}")

      } finally {
        observations.detach()
      }
    } finally {
//      activityRule.activity.finish()
//      InstrumentationRegistry.getInstrumentation().stopProfiling()
//      storeThread.quitSafely()
//      storeThread.join()
      Log.i(TAG, "Stopping Activity")
      activityRule.finishActivity()
      Log.i(TAG, "Joining store thread")
      //InstrumentationRegistry.getInstrumentation().stopProfiling()
      storeThread.quitSafely()
      storeThread.join()
      Log.i(TAG, "Done")
    }
  }
}