package org.densebrain.kdux.store

class StoreObservers(vararg observers:StoreObserver<*>) {
  private val observers = observers.toMutableList()

  operator fun plusAssign(observer:StoreObserver<*>) {
    this.observers.add(observer)
  }

  fun attach(doUpdate: Boolean = true):StoreObservers {
    observers.forEach { it.attach(doUpdate) }
    return this
  }

  fun detach() = observers.forEach { it.detach() }

}

