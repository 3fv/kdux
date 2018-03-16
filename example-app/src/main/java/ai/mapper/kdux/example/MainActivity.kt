package ai.mapper.kdux.example

import ai.mapper.kdux.example.store.ExampleActions
import ai.mapper.kdux.example.store.ExampleState
import ai.mapper.kdux.store.actions
import ai.mapper.kdux.store.observe
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.view.*
import org.jetbrains.anko.contentView
import org.jetbrains.anko.sdk25.coroutines.onClick

class MainActivity : AppCompatActivity() {


  private var countObserver = observe<Int,ExampleState>(
    { value, _, _ -> (this.contentView as View).counter.text = value.toString() },
    { state -> state.count }
  )

  private var countObserver2 = observe<ExampleState>(
    { value, _, _ -> (this.contentView as View).counter2.text = value.count2.toString() }
  )

  /**
   * Attach observers on create
   */
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    countObserver.attach()
    countObserver2.attach()
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
