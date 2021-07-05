package com.troido.bless.app.deserialization

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.troido.bless.comm.scan.deserialization.BlessCommData
import com.troido.bless.comm.scan.deserialization.BlessCommDeserializer
import com.troido.bless.app.model.Device
import com.troido.bless.scan.BleScanner
import com.troido.bless.scan.ScanCallback
import com.troido.bless.scan.ScanFilter
import com.troido.bless.scan.ScanSettings
import timber.log.Timber

class DeserializationViewModel(
    private val bleScanner: BleScanner,
    private val commDeserializer: BlessCommDeserializer
) : ViewModel(), ScanCallback<BlessCommData> {

    val isScanningLiveData = MutableLiveData(false)

    private val devices = mutableListOf<Device>()
    val devicesLiveData = MutableLiveData<List<Device>>()

    private var device: Device? = null
    val deserializedDataLiveData = MutableLiveData<DeserializationData?>()

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION])
    fun startScan() {
        bleScanner.startScan(ScanFilter.empty(), ScanSettings.default(), commDeserializer, this)
        isScanningLiveData.postValue(true)
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