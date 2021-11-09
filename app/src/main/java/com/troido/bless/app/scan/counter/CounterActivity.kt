package com.troido.bless.app.scan.counter

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import com.troido.bless.app.databinding.ActivityDeviceCounterBinding
import com.troido.bless.scan.ScanFilter
import com.troido.bless.scan.ScanResult
import com.troido.bless.scan.ScanSettings
import com.troido.bless.ui.scan.ScanActivity

class CounterActivity : ScanActivity() {
    override val scanFilter = ScanFilter.empty()
    override val scanSettings = ScanSettings.default()

    private lateinit var binding: ActivityDeviceCounterBinding

    override fun onIsScanningChanged(isScanning: Boolean) {
        Toast
            .makeText(this, "Scanning changed to: $isScanning", Toast.LENGTH_SHORT)
            .show()
    }

    @SuppressLint("SetTextI18n")
    override fun onNewDevicesList(newDevicesList: List<ScanResult>) {
        binding.count.text = "${newDevicesList.size}\ndevices"
    }

    override fun handleError(isUserDecision: Boolean) {
        Toast
            .makeText(this, "Scan Error. By User: $isUserDecision", Toast.LENGTH_LONG)
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceCounterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        startScan()
    }

    override fun onStop() {
        super.onStop()
        stopScan()
    }
}
