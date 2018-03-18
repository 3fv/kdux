package org.densebrain.kdux.store

class StoreObservers(vararg observers:StoreObserver<*,*>) {
  val observers = observers.toMutableList()

  operator fun plusAssign(observer:StoreObserver<*,*>) {
    this.observers.add(observer)
  }

  fun attach() = observers.forEach { it.attach() }

  fun detach() = observers.forEach { it.detach() }


}

