package org.densebrain.kdux.example

import android.app.Activity
import androidx.databinding.DataBindingUtil

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.ViewModelProviders
import org.densebrain.kdux.android.StateLiveData
import org.densebrain.kdux.example.databinding.ActivityMainBinding
import org.densebrain.kdux.example.store.ExampleState
import org.densebrain.kdux.store.actions
import org.densebrain.kdux.store.observations

class MainActivity : AppCompatActivity() {

  // EXAMPLE NOT USED
//  private var countObserver = observe<ExampleState,Int>(
//    { value, _ -> contentView!!.counter.text = value.toString() },
//    { state -> state.count }
//  )
//
//  // EXAMPLE NOT USED
//  private var countObserver2 = observe<ExampleState> { value, _ -> contentView!!.counter2.text = value.count2.toString() }
//
//  // EXAMPLE USED
//  private val observers = observations {
//    observe<ExampleState,Int> { state -> state.count }
//      .update { value, _ ->
//        (contentView as View).counter.text = value.toString()
//      }
//
//    observe<ExampleState>().update { value, _ ->
//      (contentView as View).counter2.text = value.count2.toString()
//    }
//
//    observeComplex<Int> {
//      val exampleState:ExampleState = get()
//
//      exampleState.count + exampleState.count2
//    } update { newValue, _ ->
//      contentView?.counter3?.text = newValue.toString()
//    }
//  }

  /**
   * Attach observers on create
   */
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
    binding.lifecycleOwner = this
    binding.model = StateLiveData(ExampleState::class)
    binding.actions = actions()
  }

  /**
   * Detach on destroy
   */
  override fun onDestroy() {
    super.onDestroy()

    //countObserver.detach()
  }
}
