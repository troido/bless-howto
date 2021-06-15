---
layout: default
title: Advertising
parent: Core
nav_order: 1
---
# Advertising

Bluetooth Low Energy (BLE) defines a couple of roles that BLE devices can operate in. Two of these roles are a broadcaster role and an observer role. A device that is operating in the broadcaster role transmits advertising events periodically. On the contrary, a device that is operating in the observer role receives the advertising events.

If an Android app wants to emit the advertising events, and take the broadcaster role, it needs to start BLE advertising.

## BleAdvertiser

`BleAdvertiser` is the central API for BLE advertising in Android. Retreive the advertiser object anywhere from your app by doing the following.

```kotlin
val advertiser = Bless.bleAdvertiser
```

Start advertising by calling `BleAdvertiser.start` method.

```kotlin
val settings = AdvertiseSettings.Builder().build()
val data = AdvertiseData.Builder().build()
val callback = MyAdvertiseCallback()
advertiser.start(settings, data, callback)
```

Use `AdvertiseSettings` for configuring advertising, and `AdvertiseData` for setting the advertisement data.

`AdvertiseCallback` is used for receiving advertisement events.

```kotlin
class MyAdvertiseCallback : AdvertiseCallback {

    fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
        // On start success
    }

    fun onStartFailure(failureCode: AdvertiseFailureCode) {
        // On start failure
    }
}
```

Stop advertising by calling `BleAdvertiser.stop`.

```kotlin
advertiser.stop()
```
