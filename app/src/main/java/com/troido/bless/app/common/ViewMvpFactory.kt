package com.troido.bless.app.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.troido.bless.app.scan.button_control.views.DeviceListItemViewMvp
import com.troido.bless.app.scan.button_control.views.DeviceListItemViewMvpImpl
import com.troido.bless.app.scan.button_control.views.DeviceScanViewMvp
import com.troido.bless.app.scan.button_control.views.DeviceScanViewMvpImpl

class ViewMvpFactory(private val layoutInflater: LayoutInflater) {

    fun getDeviceScanViewMvp(parent: ViewGroup?): DeviceScanViewMvp {
        return DeviceScanViewMvpImpl(layoutInflater, parent, this)
    }

    fun getDeviceListItemViewMvp(parent: ViewGroup?): DeviceListItemViewMvp {
        return DeviceListItemViewMvpImpl(layoutInflater, parent)
    }
}
