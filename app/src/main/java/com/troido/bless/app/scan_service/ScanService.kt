package com.troido.bless.app.scan_service

import com.troido.bless.scan.ScanFilter
import com.troido.bless.scan.ScanMode
import com.troido.bless.scan.ScanResult
import com.troido.bless.scan.ScanSettings
import com.troido.bless.ui.scan.service.BleScanService
import timber.log.Timber

class ScanService : BleScanService() {

    override val scanSettings: ScanSettings =
        ScanSettings.Builder().scanMode(ScanMode.LOW_LATENCY).build()

    override val scanFilter: ScanFilter = super.scanFilter

    override fun onScanFailed(errorCode: Int) {
        Timber.d("Scan failed, errorCode: $errorCode")
        stopSelf()
    }

    override fun onScanResult(result: ScanResult) {
        Timber.d("Scan result, result: $result")
    }
}
