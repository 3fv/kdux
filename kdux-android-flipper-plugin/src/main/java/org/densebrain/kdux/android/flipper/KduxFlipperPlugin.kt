package org.densebrain.kdux.android.flipper

import com.facebook.flipper.core.FlipperArray
import com.facebook.flipper.core.FlipperConnection
import com.facebook.flipper.core.FlipperPlugin
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import org.densebrain.kdux.android.androidObservations
import org.densebrain.kdux.store.*
import org.densebrain.kdux.store.observe
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicReference

class KduxFlipperPlugin : FlipperPlugin {

  companion object {
    private val gson = Gson()
  }

  private val connection = AtomicReference<FlipperConnection?>(null)

  private val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

  private val store
    get() = StoreContext.store

  private suspend fun sendState(state: RootState) {
    val connection = connection.get() ?: return
    val json = gson.toJson(state.entries.fold(mutableMapOf<String,Any>()) { map, (clazz,state) ->
      map[clazz.simpleName!!] = state
      map
    })

    connection.send("sendState", FlipperArray.Builder().put(json).build())
  }

  var observers = lazy {
    observations {
      observe<RootState, RootState> { state -> state } update { rootState, _ ->
        GlobalScope.launch(dispatcher) {
          sendState(rootState)
        }
      }
    }
  }

  override fun onConnect(connection: FlipperConnection?) {
    this.connection.set(connection)

    observers.value.attach()
  }

  override fun runInBackground(): Boolean = true

  override fun getId(): String = "kdux-flipper-plugin"

  override fun onDisconnect() {
    observers.value.detach()

  }
}