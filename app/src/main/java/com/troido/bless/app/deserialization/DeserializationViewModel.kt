package com.troido.bless.app.deserialization

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.troido.bless.app.model.BluetoothInfo
import com.troido.bless.app.model.Device
import com.troido.bless.comm.scan.deserialization.BlessCommData
import com.troido.bless.comm.scan.deserialization.BlessCommDeserializer
import com.troido.bless.scan.BleScanner
import com.troido.bless.scan.ScanCallback
import com.troido.bless.scan.ScanFilter
import com.troido.bless.scan.ScanSettings
import timber.log.Timber

class DeserializationViewModel(
    private val bleScanner: BleScanner,
    private val blessCommDeserializer: BlessCommDeserializer,
    private val bluetoothInfo: BluetoothInfo
) : ViewModel(), ScanCallback<BlessCommData> {

    val isScanningLiveData = MutableLiveData(false)

    private val devices = mutableListOf<Device>()
    val devicesLiveData = MutableLiveData<List<Device>>()

    private var device: Device? = null
    val deserializedDataLiveData = MutableLiveData<DeserializationData?>()

    /**
     * Starts scan and returns true if bluetooth is enabled. Otherwise returns false
     */
    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION])
    fun startScan(): Boolean {
        return if (bluetoothInfo.isBluetoothEnabled) {
            bleScanner.startScan(
                ScanFilter.empty(), ScanSettings.default(), blessCommDeserializer, this
            )
            isScanningLiveData.postValue(true)
            true
        } else false
    }

    fun stopScan() {
        bleScanner.stopScan(this)
        isScanningLiveData.postValue(false)
    }

    fun setSelectedDevice(device: Device) {
        if (this.device != device) {
            this.device = device
            deserializedDataLiveData.postValue(null)
        }
    }

    override fun onScanResult(result: BlessCommData) {
        val device = Device(
            name = result.scanResult.device.name ?: "Unknown",
            address = result.scanResult.device.address
        )
        if (device !in devices) {
            devices.add(device)
            devicesLiveData.postValue(devices)
        }
        if (this.device == device) {
            val deserializationData = DeserializationData(
                device, result.deserializedData.map { Pair(it.key, it.value.toString()) }
            )
            deserializedDataLiveData.postValue(deserializationData)
        }
    }

    override fun onScanFailed(errorCode: Int) {
        Timber.e("Scan failed with code: $errorCode.")
        isScanningLiveData.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        stopScan()
    }
}
