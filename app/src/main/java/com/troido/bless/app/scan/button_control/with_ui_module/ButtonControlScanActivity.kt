package com.troido.bless.app.scan.button_control.with_ui_module

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.troido.bless.app.common.ViewMvpFactory
import com.troido.bless.app.common.extensions.showLongToast
import com.troido.bless.app.model.Device
import com.troido.bless.app.scan.button_control.views.DeviceScanViewMvp
import com.troido.bless.scan.ScanFilter
import com.troido.bless.scan.ScanResult
import com.troido.bless.scan.ScanSettings
import com.troido.bless.ui.scan.ScanActivity
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf


class ButtonControlScanActivity : ScanActivity(), DeviceScanViewMvp.Listener {

    private val viewMvpFactory: ViewMvpFactory by inject { parametersOf(LayoutInflater.from(this)) }

    private lateinit var viewMvp: DeviceScanViewMvp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewMvp = viewMvpFactory.getDeviceScanViewMvp(null)
        viewMvp.registerListener(this)

        setContentView(viewMvp.rootView)
    }

    override val scanFilter = ScanFilter.empty()
    override val scanSettings = ScanSettings.default()

    override fun onIsScanningChanged(isScanning: Boolean) {
        viewMvp.bindScanButtonState(isScanning)
    }

    override fun onNewDevicesList(newDevicesList: List<ScanResult>) {
        viewMvp.bindDevices(newDevicesList.map {
            Device(it.device.name ?: "Unknown", it.device.address)
        })
    }

    override fun handleError(isUserDecision: Boolean) {
        showLongToast("Error")
    }

    override fun onStartScanClicked() {
        startScan()
    }

    override fun onStopScanClicked() {
        stopScan()
    }

    override fun onDeviceClicked(device: Device) {
        val data = Intent().apply {
            putExtra(RESULT_EXTRA_STRING_NAME, device.name)
            putExtra(RESULT_EXTRA_STRING_ADDRESS, device.address)
        }
        setResult(RESULT_OK, data)
        finish()
    }


    override fun onDestroy() {
        super.onDestroy()
        viewMvp.unregisterListener(this)
    }

    companion object {
        const val RESULT_EXTRA_STRING_NAME = "result_extra_name"
        const val RESULT_EXTRA_STRING_ADDRESS = "result_extra_address"
    }
}