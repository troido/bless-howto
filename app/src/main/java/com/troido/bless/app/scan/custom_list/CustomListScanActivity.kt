package com.troido.bless.app.scan.custom_list

import com.troido.bless.scan.ScanFilter
import com.troido.bless.scan.ScanResult
import com.troido.bless.scan.ScanSettings
import com.troido.bless.ui.scan.ListScanActivity

class CustomListScanActivity : ListScanActivity<ScanResult>() {
    override val adapter = CustomListAdapter(this::onDeviceSelected)

    // As an example, we only show devices, that advertise a name.
    override fun getListForAdapter(deviceList: List<ScanResult>) =
        deviceList.filter { it.device.name != null }

    override val scanFilter = ScanFilter.empty()
    override val scanSettings = ScanSettings.default()
}
