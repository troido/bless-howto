package com.troido.bless.app.deserialization.views

import com.troido.bless.app.common.views.ObservableViewMvp
import com.troido.bless.app.deserialization.DeserializationData
import com.troido.bless.app.model.Device

interface DeserializationViewMvp : ObservableViewMvp<DeserializationViewMvp.Listener> {

    interface Listener {

        fun onStartScanButtonClicked()

        fun onStopScanButtonClicked()

        fun onDeviceSelected(device: Device)
    }

    fun bindScanningState(isScanning: Boolean)

    fun bindDevices(devices: List<Device>)

    fun bindDeserializedData(deserializationData: DeserializationData?)
}