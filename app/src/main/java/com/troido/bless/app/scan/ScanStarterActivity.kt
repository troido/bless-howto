package com.troido.bless.app.scan

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.troido.bless.app.R
import com.troido.bless.app.databinding.ActivityDeviceScanStarterBinding
import com.troido.bless.app.model.ScanConfig
import com.troido.bless.app.scan.button_control.with_ui_module.ButtonControlScanActivity
import com.troido.bless.app.scan.counter.CounterActivity
import com.troido.bless.app.scan.custom_list.CustomListScanActivity
import com.troido.bless.app.scan.filter_dialog.FilterDialogFragment
import com.troido.bless.app.scan.filter_dialog.SaveScanConfigCallback
import com.troido.bless.ui.scan.ListScanActivity.Companion.RESULT_ABORT
import com.troido.bless.ui.scan.ListScanActivity.Companion.RESULT_SCANNING_ERROR
import com.troido.bless.ui.scan.ListScanActivity.Companion.RESULT_SUCCESS
import com.troido.bless.ui.scan.bless.BlessScanActivity

class ScanStarterActivity : AppCompatActivity(R.layout.activity_device_scan_starter) {

    private lateinit var binding: ActivityDeviceScanStarterBinding

    private val requestBluetoothScanning =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                RESULT_ABORT -> showToast("User Aborted Scanning")
                RESULT_SUCCESS -> {
                    val macAddress = BlessScanActivity.getAddressFromResult(it.data)
                    showToast("Retrieved Mac: $macAddress")
                }
                RESULT_SCANNING_ERROR -> showToast("Error In Scanning")
            }
        }

    private val requestScanningButtonControl =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                it.data?.let { data ->
                    val name =
                        data.getStringExtra(ButtonControlScanActivity.RESULT_EXTRA_STRING_NAME)
                    val address =
                        data.getStringExtra(ButtonControlScanActivity.RESULT_EXTRA_STRING_ADDRESS)
                    Toast.makeText(this, "Name: $name, address: $address", Toast.LENGTH_LONG).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceScanStarterBinding.bind(findViewById(R.id.root))
        binding.startBlessScan.setOnClickListener {
            requestBluetoothScanning.launch(BlessScanActivity.getIntent(this))
        }
        binding.startBlessScanWithFilter.setOnClickListener {
            showFilterDialog()
        }
        binding.startBlessScanWithAppTheme.setOnClickListener {
            requestBluetoothScanning.launch(
                BlessScanActivity.getIntent(
                    this,
                    R.style.AppThemeWithActionBar
                )
            )
        }
        binding.startScanWithCustomAdapter.setOnClickListener {
            val intent = Intent(this, CustomListScanActivity::class.java)
            requestBluetoothScanning.launch(intent)
        }
        binding.startScanWithButtonControl.setOnClickListener {
            val intent = Intent(this, ButtonControlScanActivity::class.java)
            requestScanningButtonControl.launch(intent)
        }
        binding.startDeviceCounter.setOnClickListener {
            val intent = Intent(this, CounterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showFilterDialog() {
        FilterDialogFragment.newInstance().show(supportFragmentManager,
            object : SaveScanConfigCallback {
                override fun onSaveScanConfig(scanConfig: ScanConfig) {
                    startScanWithScanConfiguration(scanConfig)
                }
            })
    }

    private fun startScanWithScanConfiguration(scanConfig: ScanConfig) {
        val (scanSettings, scanFilter) = FilterDialogFragment
            .getScanFilterAndSettingsPair(this@ScanStarterActivity, scanConfig)
        requestBluetoothScanning.launch(
            BlessScanActivity.getIntent(
                this, R.style.AppThemeWithActionBar, scanFilter, scanSettings
            )
        )
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}