package com.troido.bless.app.common.views

abstract class BaseObservableViewMvp<T> : ObservableViewMvp<T> {

    protected val listeners = mutableSetOf<T>()

    override fun registerListener(listener: T) {
        listeners.add(listener)
    }

    override fun unregisterListener(listener: T) {
        listeners.remove(listener)
    }
}
