package com.troido.bless.app.bonding

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.troido.bless.Bless
import com.troido.bless.BluetoothDevice
import com.troido.bless.app.R
import com.troido.bless.app.bonding.exception.BluetoothNotEnabledException
import com.troido.bless.app.common.extensions.showLongToast
import com.troido.bless.app.common.extensions.showToast
import com.troido.bless.app.databinding.ActivityBondingBinding
import com.troido.bless.app.main.PermissionsActivity
import com.troido.bless.bonding.BondingResultCallback
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.UUID

class BondingActivity : PermissionsActivity(
    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
    R.string.location_permission_rationale
) {

    private val deviceList = mutableListOf<BluetoothDevice>()
    private var currentBondedDevice: BluetoothDevice? = null
    private val bondingResultCallback = object : BondingResultCallback {
        override fun onBonding(bluetoothDevice: BluetoothDevice) {
            showToast("Bonding with device ${bluetoothDevice.address}")
        }

        override fun onBonded(bluetoothDevice: BluetoothDevice) {
            showToast("Bonded with ${bluetoothDevice.address}")
        }

        override fun onBondingFailed(bluetoothDevice: BluetoothDevice) {
            showToast("Bonding to ${bluetoothDevice.address} failed")
        }
    }

    private val viewModel by viewModel<BondingViewModel>()

    private lateinit var binding: ActivityBondingBinding

    private val requestBluetooth =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                showToast(getString(R.string.bluetooth_enabled))
                binding.scanButton.performClick()
            } else showToast(getString(R.string.bluetooth_disabled))
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBondingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.errorLiveData.observe(this) {
            if (it.cause is BluetoothNotEnabledException) {
                requestBluetooth.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            } else it.message?.let(::showLongToast)
        }

        viewModel.deviceLiveData.observe(this) {
            deviceList.add(it)
            (binding.deviceListRecyclerView.adapter as BondingRecyclerViewAdapter)
                .addDevices(*deviceList.toTypedArray())
        }

        setUpRecycler()

        binding.scanButton.setOnClickListener {
            runWithPermissions {
                startScanning()
            }
        }
    }

    /**
     * [PermissionsActivity] handles permissions automatically, so we suppress the warning here
     */
    @SuppressLint("MissingPermission")
    private fun startScanning() {
        if (binding.scanButton.text == getString(R.string.scan_button_text)) {
            if (viewModel.startScanning()) {
                binding.scanButton.text = getString(R.string.stop_scan_button_text)
            }
        } else {
            viewModel.stopScanning()
            binding.scanButton.text = getString(R.string.scan_button_text)
        }
    }

    private fun onDeviceClicked(bluetoothDevice: BluetoothDevice) {
        currentBondedDevice = bluetoothDevice
        if (viewModel.isDeviceBonded(bluetoothDevice)) {
            viewModel.establishConnection(bluetoothDevice) { _, connection ->
                connection.write(
                    UUID.fromString("a9aa6c01-23e8-4cde-8386-e2eefa83f0d7"),
                    "Bond".toByteArray()
                )
                showLongToast("Sending \"Bond\" to device as Byte Array")

                Bless.deviceBonder.removeBondedDevice(bluetoothDevice.address)
                showToast("Removed Bond to Device")
            }
        } else {
            viewModel.bondDevice(bluetoothDevice, bondingResultCallback)
        }
    }

    private fun setUpRecycler() {
        binding.deviceListRecyclerView.adapter =
            BondingRecyclerViewAdapter(mutableListOf(), this::onDeviceClicked)
        binding.deviceListRecyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.deviceListRecyclerView.setHasFixedSize(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopScanning()
    }

    companion object {
        @JvmStatic
        fun startActivity(context: Context) {
            val intent = Intent(context, BondingActivity::class.java)
            context.startActivity(intent)
        }
    }
}
