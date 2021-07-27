---
layout: default
title: Bonding
parent: Core
nav_order: 4
---

# Bonding

Bonding is the exchange of long term encryption keys

## Bonding vs. pairing

You may have seen the terms “bonding” and “pairing” used interchangeably and this led to consumers and even developers thinking that bonding and pairing are one and the same. From the consumer perspective, they may as well be, but it’s technically inaccurate.

Pairing is the exchange of temporary encryption keys that allows for the exchange of long term encryption keys — the bonding process — to take place.

This really just means that after the pairing features exchange and the connection has been encrypted (these two together are called ‘pairing’), and keys have been exchanged, the devices STORE and USE those keys the next time they connect. Keys can be exchanged using the bonding procedure, but that does not mean they are bonded if the keys are not stored and used the next time.

If a device is bonded with another device, like a heart rate monitor and a smartphone, they can encrypt the connection without exchanging any sensitive security information. When the smartphone connects to the heart rate monitor, it can just issue a ‘turn on encryption’ request, and both sides will use the keys already stored, so nobody snooping can see a key exchange and therefore decode the messages being sent, as is done when pairing.


### Initiating a bond

The request to bond can come from either the central or the peripheral, depending on the system design. There are multiple ways to initiate the bonding process, listed here in descending order of our preference:

1. **Android-initiated**. Have Android start the bonding process automatically when the BLE device emits an Insufficient Authentication error due to an unauthorized ATT request.
2. **Developer-initiated**. Proactively start the bonding process ourselves by calling BluetoothDevice’s `createBond()` method.
3. **Peripheral-initiated**. If you have access to the source code of the firmware running on the BLE device, you can have it send a security request proactively that’ll kickstart the bonding process as soon as it’s connected to a central.

With Bless you can initiate (developer-initiated) bonding pretty easily:

1. Create `BondingResultCallback` callback;
```kotlin
private val bondingResultCallback = object : BondingResultCallback {
    override fun onBonding(bluetoothDevice: BluetoothDevice) {
        showToast("Bonding with device ${bluetoothDevice.address}")
    }

    override fun onBonded(bluetoothDevice: BluetoothDevice) {
        showToast("Bonded with ${bluetoothDevice.address}")
    }

    override fun onBondingFailed(bluetoothDevice: BluetoothDevice) {
        showToast("Bonding to ${bluetoothDevice.address} failed")
    }
}
```
2. Register your callback;
`Bless.deviceBonder.registerBondingCallback(bondingResultCallback)`

3. Bond to your device*.
`Bless.deviceBonder.bondDevice(bluetoothDevice)`

\* *Not All devices are bondable, so with bonding you need to be sure what you are doing.*

### List of bonded devices

To get a list of bonded devices, just use the following code.

```kotlin
val bondedDevices = Bless.deviceBonder.bondedDevices
```
### Remove Bond

When you want to remove your bonded device you can use the following code.

```kotlin
Bless.deviceBonder.removeBondedDevice(bluetoothDevice)
```

### Remove listeners

To avoid memory leak you should remove your callbacks/listeners when you are done with bonding

```kotlin
Bless.deviceBonder.unregisterBondingCallback(bondingCallback)
```
