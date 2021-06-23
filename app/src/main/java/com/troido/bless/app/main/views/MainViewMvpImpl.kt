package com.troido.bless.app.main.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.troido.bless.app.common.views.BaseObservableViewMvp
import com.troido.bless.app.databinding.LayoutMainBinding

class MainViewMvpImpl(
    layoutInflater: LayoutInflater,
    parent: ViewGroup?
) : MainViewMvp, BaseObservableViewMvp<MainViewMvp.Listener>() {

    private val binding = LayoutMainBinding.inflate(layoutInflater, parent, false)

    override val rootView: View = binding.root

    init {
        binding.scanFeature.setOnClickListener {
            listeners.forEach { it.onScanFeatureClicked() }
        }
        binding.deserializationFeature.setOnClickListener {
            listeners.forEach { it.onDeserializationFeatureClicked() }
        }
        binding.scanServiceFeature.setOnClickListener {
            listeners.forEach { it.onScanServiceFeatureClicked() }
        }
        binding.connectServiceFeature.setOnClickListener {
            listeners.forEach { it.onConnectServiceFeatureClicked() }
        }
        binding.bondingFeature.setOnClickListener {
            listeners.forEach { it.onBondingFeatureClicked() }
        }
    }

    override fun showToastMessage(message: String) {
        Toast.makeText(rootView.context, message, Toast.LENGTH_LONG).show()
    }
}