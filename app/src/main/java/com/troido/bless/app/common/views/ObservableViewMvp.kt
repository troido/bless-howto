package com.troido.bless.app.common.views

interface ObservableViewMvp<T> : ViewMvp {

    fun registerListener(listener: T)

    fun unregisterListener(listener: T)
}
