---
layout: default
title: Logging
nav_exclude: true
---
# BLE Logging
Get logs from the remote device by using BLE Logging.

## How to Use
1. Initialize BLESS library in your Android app.
```kotlin
Bless.initialize(applicationContext)
```
2. Retreive `BleLogging` object from `Bless` by passing in a Bluetooth address of a remote device.
```kotlin
val bleLogging = Bless.getBleLogging(deviceAddress)
```
3. Register a listener for logging events.
```kotlin
val myListener = object : BleLogging.Listener {

    override fun onNextLog(log: String) {
        // On next log
    }
}
bleLogging.registerListener(myListener)
```
4. Start logging by calling `startLogging` method.
```kotlin
bleLogging.startLogging()
```
5. When finished, stop the logging and unregister your listener.
```kotlin
bleLogging.stopLogging()
bleLogging.unregisterListener(myListener)
```
