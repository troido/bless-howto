---
layout: default
nav_order: 1
---
# BLESS
BLESS is a set of Android libraries that makes your work with ANdroid BLE framework easy.

BLESS implements common use-cases including: device discovery, connectivity, data transfer and remote device control.

BLESS contains both UI components and low-level APIs.

## Libraries

#### bless-core
Core features including BLE device discovery, connection, data transfer, and many more.

#### bless-ui
Out-of-the-box Android UI components including views, activities, and services.

#### bless-aconno
Custom library implementing features for aconno IoT devices.

## Quickstart
First you need to [import](./docs/setup.md) the modules.

Using BLESS in your code is easy. You just need to initialize the library on your app start.
```kotlin
class MyApplication : Application() {

    override fun onCreate() {
        Bless.initialize(this)
    }
}
```
You can use `Bless` object to get various components of the library.

For further documentation take a look at [Documentation References](./docs/reference.md).
