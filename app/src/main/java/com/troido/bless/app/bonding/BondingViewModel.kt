package com.troido.bless.app.bonding

import android.Manifest
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.troido.bless.Bless
import com.troido.bless.BluetoothDevice
import com.troido.bless.bonding.BondingResultCallback
import com.troido.bless.connection.BleConnection
import com.troido.bless.connection.GattCharacteristic
import com.troido.bless.connection.GattService
import com.troido.bless.scan.ScanCallback
import com.troido.bless.scan.ScanFilter
import com.troido.bless.scan.ScanResult
import com.troido.bless.scan.ScanSettings

class BondingViewModel: ViewModel() {

    private val mutableDeviceSet = mutableSetOf<BluetoothDevice>()
    private val deviceLiveData = MutableLiveData<BluetoothDevice>()

    private val _errorLiveData = MutableLiveData<Error>()
    val errorLiveData: LiveData<Error>
        get() = _errorLiveData

    private val scanResultCallback = object: ScanCallback<ScanResult> {
        override fun onScanFailed(errorCode: Int) {
            _errorLiveData.value = Error("Error code $errorCode given when scanning for devices.")
        }

        override fun onScanResult(result: ScanResult) {
            if (mutableDeviceSet.none { device -> device.address == result.device.address }) {
                mutableDeviceSet.add(result.device)
                deviceLiveData.postValue(result.device)
            }
        }
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION])
    fun startScanning(): LiveData<BluetoothDevice> {
        // Check for permissions
        Bless.bleScanner.startScan(
            ScanFilter.empty(),
            ScanSettings.default(),
            scanResultCallback
        )

        return deviceLiveData
    }

    fun stopScanning() {
        Bless.bleScanner.stopScan(scanResultCallback)
    }

    fun bondDevice(bluetoothDevice: BluetoothDevice, bondingCallback: BondingResultCallback) {
        Bless.deviceBonder.registerBondingCallback(bondingCallback)
        Bless.deviceBonder.bondDevice(bluetoothDevice)
    }

    fun establishConnection(
        bluetoothDevice: BluetoothDevice,
        onConnected: (List<GattService>, BleConnection) -> Unit
    ): BleConnection {
        val connection = Bless.createBleConnection(bluetoothDevice.address)
        connection.registerListener(BleConnectionListener(
            bluetoothDevice.address,
            _errorLiveData) {
                onConnected(it, connection)
            })
        connection.connect()
        return connection
    }

    fun isDeviceBonded(bluetoothDevice: BluetoothDevice): Boolean {
        return Bless.deviceBonder.isDeviceBonded(bluetoothDevice.address)
    }

    internal class BleConnectionListener(
        val deviceAddress: String,
        private val errorLiveData: MutableLiveData<Error>,
        val onConnectedCallback: (List<GattService>) -> Unit
    ): BleConnection.Listener {
        override fun onConnected(services: List<GattService>) {
            this.onConnectedCallback(services)
        }

        override fun onDisconnected() { }

        override fun onRead(characteristic: GattCharacteristic, data: ByteArray) { }

        override fun onWrite(characteristic: GattCharacteristic) { }

        override fun onNotifyEnabled(characteristic: GattCharacteristic) { }

        override fun onNotify(characteristic: GattCharacteristic, data: ByteArray) { }

        override fun onMtuChanged(mtu: Int) { }

        override fun onError(message: String) {
            errorLiveData.postValue(Error("Connection error: $message"))
        }
    }
}