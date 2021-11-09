package com.troido.bless.app.scan.button_control.views

import com.troido.bless.app.common.views.ObservableViewMvp
import com.troido.bless.app.model.Device

interface DeviceListItemViewMvp : ObservableViewMvp<DeviceListItemViewMvp.Listener> {

    interface Listener {

        fun onDeviceClicked(device: Device)
    }

    fun bindDevice(device: Device)
}
