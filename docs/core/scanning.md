# Scanning

Bluetooth Low Energy (BLE) defines a couple of roles that BLE devices can operate in. Two of these roles are a broadcaster role and an observer role. A device that is operating in the broadcaster role transmits advertising events periodically. On the contrary, a device that is operating in the observer role receives the advertising events.

If an Android app wants to receive the advertising events, and take the observer role, it needs to start BLE scanning.

## BleScanner

`BleScanner` is the central API for BLE scanning in Android. Retreive the scanner object anywhere from your app by doing the following.
```kotlin
val scanner = Bless.bleScanner
```

Start scanning by calling `startScan` method. Make sure that your app holds a location permission.
```kotlin
val filter = ScanFilter.empty()
val settings = ScanSettings.default()
val callback = MyScanCallback()
scanner.startScan(filter, settings, callback)
```
Use `ScanFilter` for filtering the scan results, and `ScanSettings` for configuring scanning.

`ScanCallback` is used for receiving scan results.
```kotlin
class MyScanCallback : ScanCallback<ScanResult> {

    override fun onScanFailed(errorCode: Int) {
        // Report the error
    }

    override fun onScanResult(result: ScanResult) {
        // New scan result
    }
}
```

Stop scanning by calling `stopScan` on `BleScanner` object.
```kotlin
scanner.stopScan(callback)
```

## Deserialisation

Deserialisation is a feature that enables converting `ScanResult` to a client defined data type.

Implement `Deserializer` interface and pass the deserialiser object in `startScan` method.

```kotlin
val addressDeserializer = object : Deserializer<String> {

    override fun deserialize(scanResult: ScanResult): String? {
        return scanResult.device.address
    }
}
val callback = object : ScanCallback<String> {

    override fun onScanFailed(errorCode: Int) {
        // Report the error
    }

    override fun onScanResult(result: String) {
        // New address
    }
}
scanner.startScan(filter, settings, addressDeserializer, callback)
```

Additionally, `Deserializer` object can be used for advanced filtering. If `deserialize` method returns `null` value, this will be ignored and `ScanCallback.onScanResult` method won't be called.

## Example

BLE device has a temperature sensor and emits temperature values in its advertisements every 5 seconds. Temperature values are encoded as a float value in first 4 bytes of manufacturer specific data field in each advertisement. Device address is `00:11:22:33:AA:BB`.

Let's model our data.

```kotlin
data class SensorReading(
    val temperature: Float
)
```

We need to implement a deserilaiser to extract the temperature from a `ScanResult`.

```kotlin
class SensorDeserializer : Deserializer<SensorReading> {

    override fun deserialize(scanResult: ScanResult): SensorReading? {
        val type = ScanResult.DATA_TYPE_MANUFACTURER_SPECIFIC_DATA
        val msd = scanResult.getAdvertisingData(type)
        if (msd != null && msd.size >= 4) {
            val temperature = msd.sliceArray(0 until 4).toFloat()
            return SensorReading(temperature)
        } else {
            return null
        }
    }
}
```

We can receive our sensor readings and log the temperature.

```kotlin
class SensorCallback : ScanCallback<SensorReading> {

    private val TAG = "SensorCallback"

    override fun onScanFailed(errorCode: Int) {
        Log.e(TAG, "Scan failed with error: $errorCode")
    }

    override fun onScanResult(result: SensorReading) {
        Log.d(TAG, "Temperature: ${result.temperature}")
    }
}
```

Finally, we start scanning with our deserialiser and callback.

```kotlin
val filter = ScanFilter.Builder().addDeviceAddress("00:11:22:33:AA:BB").build()
val settings = ScanSettings.default()
val deserialiser = SensorDeserializer()
val callback = SensorCallback()
Bless.bleScanner.startScan(filter, settings, deserialiser, callback)
```
