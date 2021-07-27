package com.troido.bless.app.utils

import android.bluetooth.BluetoothAdapter
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector


fun enableTurnOnBluetoothSystemDialogIfAppears() {
    if (!BluetoothAdapter.getDefaultAdapter().isEnabled) {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        runCatching {
            device.findObject(
                UiSelector()
                    .clickable(true)
                    .checkable(false)
                    .index(1)
            ).click()
        }.onSuccess {
            print("Enable bluetooth dialog found")
        }.onFailure {
            println("Enable ui dialog not found")
        }
    }
}