package com.troido.bless.app.bonding

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.troido.bless.Bless
import com.troido.bless.BluetoothDevice
import com.troido.bless.app.R
import com.troido.bless.app.databinding.ActivityBondingBinding
import com.troido.bless.app.main.PermissionsActivity
import com.troido.bless.bonding.BondingResultCallback
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class BondingActivity : PermissionsActivity(
    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
    R.string.location_permission_rationale
) {

    private val deviceList = mutableListOf<BluetoothDevice>()
    private var currentBondedDevice: BluetoothDevice? = null
    private val bondingResultCallback = object : BondingResultCallback {
        override fun onBonding(bluetoothDevice: BluetoothDevice) {
            Toast.makeText(
                this@BondingActivity,
                "Bonding with device ${bluetoothDevice.address}",
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun onBonded(bluetoothDevice: BluetoothDevice) {
            Toast.makeText(
                this@BondingActivity,
                "Bonded with ${bluetoothDevice.address}",
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun onBondingFailed(bluetoothDevice: BluetoothDevice) {
            Toast.makeText(
                this@BondingActivity,
                "Bonding to ${bluetoothDevice.address} failed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val viewModel by viewModel<BondingViewModel>()

    private lateinit var binding: ActivityBondingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBondingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.errorLiveData.observe(this) {
            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
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
            viewModel.startScanning().observe(this) {
                deviceList.add(it)
                (binding.deviceListRecyclerView.adapter as BondingRecyclerViewAdapter)
                    .addDevices(*deviceList.toTypedArray())
            }
            binding.scanButton.text = getString(R.string.stop_scan_button_text)
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

                Toast.makeText(
                    this@BondingActivity,
                    "Sending \"Bond\" to device as Byte Array",
                    Toast.LENGTH_LONG
                ).show()

                Bless.deviceBonder.removeBondedDevice(bluetoothDevice.address)
                Toast.makeText(this@BondingActivity, "Removed Bond to Device", Toast.LENGTH_SHORT)
                    .show()
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