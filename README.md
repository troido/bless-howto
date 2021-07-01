BLEss
=====

[View BLEss's documentation](https://troido.github.io/bless-howto/) | [Report an issue with BLEss](https://github.com/troido/bless-howto/issues)

BLEss is an easy to use Android BLE library. It is blazing fast, effortless, efficient and will deal with painful Android BLE implementation for you!

<p align="center">
    <h1 align="center">
    <a href="https://troido.github.io/bless-howto/"><img width="666" height="256" src="/images/bless.png" alt="Girl in a jacket"></a>
</p>

# May your BLE implementation project be BLEssed and successful!
Are you overwhelmed with Android's Bluetooth Low Energy implementation and complexity? Then our BLEss library is the perfect solution for you. It is easy to use and saves you lots of time, energy and/or money. Our libary enables you to implement Bluetooth Low Energy in your application with just a couple of lines of code. That's it! You don't have to worry about low-level BLE implementations or dive deep into BLE's complexity. By using our BLEss library you can stay focused on what you are best at - your project's business goals  and logic.

Additionally, BLEss provides you with UI components to make operations even easier with ready to go screens. Please find, following the example, a complete solution for scanning and selecting a BLE device to interact with:
```kotlin
private val requestBluetoothScanning = 
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK && it.data != null) {
            val deviceAddress = BlessScanActivity.getAddressFromResult(it.data)
        }
}
```
``` kotlin
override fun onCreate(savedInstanceState: Bundle?) {
	super.onCreate(savedInstanceState)
	...
	requestBluetoothScanning.launch(BlessScanActivity.getIntent(this))
}
```
You see! That's it. Seven lines of code just saved you hours of research and implementation. Please find more examples like this in our library documentation or in our sample project application.
## Documentation
Find our full documentation with how-to guides for all of the components of BLEss [here](https://troido.github.io/bless-howto/).
## Reporting Issues
Found a bug on a specific feature? Please share by opening an issue on  [Github](https://github.com/troido/bless-howto/issues).
## License
BLEss is a licensed software. You can use it for free without any restrictions in debug mode. For production apps you need to contact us and get a License Key. We also provide special offers for Open-Source software.
## Contact Us
For licensing and other questions [contact](https://troido.github.io/bless-howto/docs/contacts.html) us.
## About Us
Troido is a company that focuses on Android and IoT for more than 10 years. BLEss is developed by Bluetooth and Android experts in Troido.
