package org.tfv.kdux.util

open class Event<T> {
  private val handlers = mutableListOf<(T) -> Unit>()

  infix fun on(handler: (T) -> Unit) {
    invokeSynchronized(this) {
      handlers.add(handler)
    }
  }

  infix fun off(handler: (T) -> Unit) {
    invokeSynchronized(this) {
      handlers.removeAll { it == handler }
    }
  }

  fun emit(event: T) {
    lateinit var handlers:List<(T) -> Unit>

    invokeSynchronized(this) {
      handlers = this.handlers.toList()
    }

    for (handler in handlers) {
      handler(event)
    }
  }
}