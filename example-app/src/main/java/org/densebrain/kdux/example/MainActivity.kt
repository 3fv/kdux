package org.densebrain.kdux.example

import org.densebrain.kdux.example.R
import org.densebrain.kdux.example.store.ExampleActions
import org.densebrain.kdux.example.store.ExampleState
import org.densebrain.kdux.store.actions
import org.densebrain.kdux.store.observe
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.view.*
import org.densebrain.kdux.store.observations
import org.jetbrains.anko.contentView
import org.jetbrains.anko.sdk25.coroutines.onClick

class MainActivity : AppCompatActivity() {

  // EXAMPLE NOT USED
  private var countObserver = observe<ExampleState,Int>(
    { value, _ -> (contentView as View).counter.text = value.toString() },
    { state -> state.count }
  )

  // EXAMPLE NOT USED
  private var countObserver2 = observe<ExampleState>(
    { value, _ -> (contentView as View).counter2.text = value.count2.toString() }
  )

  // EXAMPLE USED
  private val observers = observations {
    observe<ExampleState,Int> { state -> state.count }
      .update { value, _ ->
        (contentView as View).counter.text = value.toString()
      }

    observe<ExampleState>().update({ value, _ ->
      (contentView as View).counter2.text = value.count2.toString()
    })

    observeComplex<Int> {
      val exampleState:ExampleState = get()

      exampleState.count + exampleState.count2
    } update { newValue, _ ->
      contentView?.counter3?.text = newValue.toString()
    }
  }

  /**
   * Attach observers on create
   */
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    observers.attach()

    contentView!!.incrementer.onClick {
      actions<ExampleActions>().incrementCounter(1)
      actions<ExampleActions>().incrementCounter2(1)
    }
  }

  /**
   * Detach on destroy
   */
  override fun onDestroy() {
    super.onDestroy()

    countObserver.detach()
  }
}
