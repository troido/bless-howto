---
layout: default
title: Connection
parent: Core
nav_order: 3
---
# Connection

Bluetooth Low Energy (BLE) defines a couple of roles that BLE devices can operate in. Two of these roles are a central role and a peripheral role. A device that is operating in the central role initiates establishment of a physical connection. On the contrary, a device that is operating in the peripheral role accepts a connection request from another device.

Android app can take the central role by using `BleConnection` API.

## Quick Start

### 1. Initialize BLESS

Initialize BLESS library in your Android app to use its connection API.

```kotlin
Bless.initialize(applicationContext)
```

### 2. Instantiate BleConnection

Instantiate `BleConnection` by calling appropriate method in `Bless` object.

```kotlin
val remoteDeviceAddress = "00:11:22:33:AA:BB"
val connection = Bless.createBleConnection(remoteDeviceAddress)
```

## Secure connection

Create a `BleConnection` with a reconnection logic.

```kotlin
val connection = Bless.createBleConnection(
    remoteDeviceAddress,
    arrayOf(1, 3, 5),
    TimeUnit.SECONDS
)
```

When a BLE connection drops, the reconnection logic will try to reconnect 3 times with 1, 3, and 5 seconds delay between attempts.

## L2BLE Connection

`L2BleConnection` represents 2-way BLE connection. It uses 2 GATT characteristics, one for sending, and one for receiving data. It is optimised for high data throughput by setting a high connection priority and requesting higher MTU value.

You can instantiate `L2BleConnection` the same way as `BleConnection`, by calling appropriate method in `Bless` object.

```kotlin
val l2BleConnection = Bless.createL2BleConnection(remoteDeviceAddress)
```
