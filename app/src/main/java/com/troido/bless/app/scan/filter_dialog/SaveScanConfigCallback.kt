package com.troido.bless.app.scan.filter_dialog

import com.troido.bless.app.model.ScanConfig

interface SaveScanConfigCallback {
    fun onSaveScanConfig(scanConfig: ScanConfig)
}
