---
layout: default
title: Timestamp Synchronization
nav_exclude: true
---
# BLE Timestamp Synchronization
Synchronize timestamp on your remote device.

## How to Use
1. Initialize BLESS library in your Android app.
```kotlin
Bless.initialize(applicationContext)
```
2. Retreive `BleTimestampSync` object from `Bless` by passing in a Bluetooth address of a remote device.
```kotlin
val bleTimestampSync = Bless.getTimestampSync(deviceAddress)
```
3. Register a listener for timestamp synchronize events.
```kotlin
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
```kotlin
bleTimestampSync.startSync()
```
5. When finished, unregister your listener.
```kotlin
bleTimestampSync.unregisterListener(myListener)
```
