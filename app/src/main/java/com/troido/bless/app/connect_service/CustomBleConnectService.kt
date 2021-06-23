package com.troido.bless.app.connect_service

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import com.troido.bless.connection.GattCharacteristic
import com.troido.bless.connection.GattService
import com.troido.bless.ui.connect.service.BleConnectService
import timber.log.Timber

class CustomBleConnectService : BleConnectService() {

    override fun onConnected(services: List<GattService>) {
        Timber.d("Connected, services: $services")
        Handler(Looper.getMainLooper()).postDelayed({
            val char = getFirstWriteChar(services)
            if (char != null) {
                write(char.uuid, byteArrayOf(1, 2, 3, 4, 5))
            }
        }, 3000)
    }

    private fun getFirstWriteChar(services: List<GattService>): GattCharacteristic? {
        services.forEach { service ->
            service.characteristics.forEach { char ->
                if (char.properties.contains(GattCharacteristic.Property.WRITE_NO_RESPONSE)) {
                    return char
                }
            }
        }
        return null
    }

    override fun onDisconnected() {
        Timber.d("Disconnected")
        stopSelf()
    }

    override fun onRead(characteristic: GattCharacteristic, data: ByteArray) {
        Timber.d("Read, char: $characteristic, data: $data")
    }

    override fun onWrite(characteristic: GattCharacteristic) {
        Timber.d("Write, char: $characteristic")
        disconnect()
    }

    override fun onNotifyEnabled(characteristic: GattCharacteristic) {
        Timber.d("Notify enabled, char: $characteristic")
    }

    override fun onNotify(characteristic: GattCharacteristic, data: ByteArray) {
        Timber.d("Notify, char: $characteristic, data: $data")
    }

    override fun onMtuChanged(mtu: Int) {
        Timber.d("Mtu changed, mtu: $mtu")
    }

    override fun onError(message: String) {
        Timber.e("Error, message: $message")
    }

    companion object {

        fun startService(context: Context, address: String) {
            val intent = Intent(context, CustomBleConnectService::class.java)
            intent.putExtra(EXTRA_REMOTE_DEVICE_ADDRESS, address)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }
}