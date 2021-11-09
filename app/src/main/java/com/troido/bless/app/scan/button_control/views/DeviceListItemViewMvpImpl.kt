package com.troido.bless.app.scan.button_control.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.troido.bless.app.common.views.BaseObservableViewMvp
import com.troido.bless.app.databinding.ItemDeviceBinding
import com.troido.bless.app.model.Device

class DeviceListItemViewMvpImpl(
    layoutInflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvp<DeviceListItemViewMvp.Listener>(), DeviceListItemViewMvp {

    private val binding = ItemDeviceBinding.inflate(layoutInflater, parent, false)

    override val rootView: View = binding.root

    private var device: Device? = null

    init {
        rootView.setOnClickListener {
            device?.let { device ->
                listeners.forEach { it.onDeviceClicked(device) }
            }
        }
    }

    override fun bindDevice(device: Device) {
        this.device = device
        binding.deviceNameTextView.text = device.name
        binding.deviceAddressTextView.text = device.address
    }
}
