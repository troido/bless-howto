# BLE Timestamp Synchronization
Synchronize timestamp on your remote device.

## How to Use
1. Initialize BLESS library in your Android app.
```Kotlin
Bless.initialize(applicationContext)
```
2. Retreive `BleTimestampSync` object from `Bless` by passing in a Bluetooth address of a remote device.
```Kotlin
val bleTimestampSync = Bless.getTimestampSync(deviceAddress)
```
3. Register a listener for timestamp synchronize events.
```Kotlin
val myListener = object : BleTimestampSync.Listener {

    override fun onSyncFinished() {
        // On sync finished
    }

    override fun onError(message: String) {
        // On error
    }
}
bleTimestampSync.registerListener(myListener)
```
4. Start logging by calling `startSync` method.
```Kotlin
bleTimestampSync.startSync()
```
5. When finished, unregister your listener.
```Kotlin
bleTimestampSync.unregisterListener(myListener)
```
