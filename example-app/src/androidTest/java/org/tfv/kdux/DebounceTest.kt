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

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class DebounceTest {

  companion object {
    private val TAG = DebounceTest::class.java.name
  }

  @get:Rule
  val activityRule = ActivityTestRule(MainActivity::class.java)


  @Test
  fun ensureDebounce_10to1() {
    //val instrumentation = InstrumentationRegistry.getInstrumentation()
    val context = activityRule.activity//instrumentation.context
    val storeThread = HandlerThread(DebounceTest::class.java.name).apply {
      start()
    }

    StoreContext.configureAndroidWithLooper(
      context,
      storeThread.looper,
      ExampleActions::class,
      ExampleActions2::class
    )
    try {
      var updateCount = 0

      val observations = androidObservations {
        observe<ExampleState, Int> { state -> state.count } update { newCount, oldCount ->
          Log.i(TAG, "Update received: ${newCount}/${oldCount}")
          updateCount++
        } debounce AndroidStoreObserver.DebounceOptions(1500L, immediateFirst = false)
      }
      try {
        observations.attach(false)


        val actions = actions<ExampleActions>()

        0.until(10).forEach { index ->
          Log.i(TAG, "Sent update ${index}")
          actions.incrementCounter(1)
          Thread.sleep(100L)
          assertEquals("Check ${index}", 0, updateCount)
        }

        Thread.sleep(1600L)
        assertEquals("Check 5", 1, updateCount)
        assertEquals("Check 6", 10, state<ExampleState>().count)

        Log.i(TAG, "Final value: ${state<ExampleState>().count}")

      } finally {
        observations.detach()
      }
    } finally {
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
