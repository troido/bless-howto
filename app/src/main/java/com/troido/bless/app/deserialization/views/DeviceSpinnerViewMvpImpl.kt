package com.troido.bless.app.deserialization.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.troido.bless.app.databinding.ItemDeviceAppbarSpinnerBinding
import com.troido.bless.app.model.Device

class DeviceSpinnerViewMvpImpl(
    layoutInflater: LayoutInflater,
    parent: ViewGroup?
) : DeviceSpinnerViewMvp {

    private val binding = ItemDeviceAppbarSpinnerBinding.inflate(layoutInflater, parent, false)

    override val rootView: View = binding.root


    override fun bindDevice(device: Device) {
        binding.deviceNameTextView.text = device.name
        binding.deviceAddressTextView.text = device.address
    }
}