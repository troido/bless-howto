package com.troido.bless.app.connect_service

import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.troido.bless.app.databinding.LayoutConnectServiceBinding
import com.troido.bless.ui.scan.bless.BlessScanActivity
import timber.log.Timber

class ConnectServiceActivity : AppCompatActivity() {

    private lateinit var binding: LayoutConnectServiceBinding

    private val requestBluetoothScanning =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK && it.data != null) {
                val address = BlessScanActivity.getAddressFromResult(it.data)
                if (address != null) {
                    CustomBleConnectService.startService(this, address)
                } else {
                    val message = "Failed to retrieve Bluetooth address from scan module"
                    Timber.e(message)
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutConnectServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startServiceConnectButton.setOnClickListener {
            requestBluetoothScanning.launch(BlessScanActivity.getIntent(this))
        }
    }
}