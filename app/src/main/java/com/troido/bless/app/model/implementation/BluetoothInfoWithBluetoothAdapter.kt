package com.troido.bless.app.model.implementation

import android.bluetooth.BluetoothAdapter
import com.troido.bless.app.model.BluetoothInfo

class BluetoothInfoWithBluetoothAdapter : BluetoothInfo {
    override val isBluetoothEnabled: Boolean
        get() = BluetoothAdapter.getDefaultAdapter()?.isEnabled ?: false
}
