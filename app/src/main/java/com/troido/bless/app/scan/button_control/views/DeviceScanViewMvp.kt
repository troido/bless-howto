package com.troido.bless.app.scan.button_control.views

import com.troido.bless.app.common.views.ObservableViewMvp
import com.troido.bless.app.model.Device

interface DeviceScanViewMvp : ObservableViewMvp<DeviceScanViewMvp.Listener> {

    interface Listener {

        fun onStartScanClicked()

        fun onStopScanClicked()

        fun onDeviceClicked(device: Device)
    }

    fun bindDevices(devices: List<Device>)

    fun bindScanButtonState(isScanning: Boolean)
}
