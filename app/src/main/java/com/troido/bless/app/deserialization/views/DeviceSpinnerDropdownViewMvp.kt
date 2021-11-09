package com.troido.bless.app.deserialization.views

import com.troido.bless.app.common.views.ViewMvp
import com.troido.bless.app.model.Device

interface DeviceSpinnerDropdownViewMvp : ViewMvp {

    fun bindDevice(device: Device)
}
