package com.troido.bless.app.main.views

import com.troido.bless.app.common.views.ObservableViewMvp

interface MainViewMvp : ObservableViewMvp<MainViewMvp.Listener> {

    interface Listener {

        fun onScanFeatureClicked()

        fun onDeserializationFeatureClicked()

        fun onScanServiceFeatureClicked()

        fun onBondingFeatureClicked()

        fun onConnectServiceFeatureClicked()
    }

    fun showToastMessage(message: String)
}