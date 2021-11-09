package com.troido.bless.app.utils

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class BluetoothStateChanger(val enable: Boolean, private val targetAppContext: Context) {
    @Volatile
    private lateinit var sleepingThread: Thread

    @Volatile
    private var operationResult: Boolean? = null

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent.getIntExtra(
                    BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR
                )
                when (state) {
                    BluetoothAdapter.STATE_OFF -> {
                        operationResult = !enable
                        sleepingThread.interrupt()
                    }
                    BluetoothAdapter.STATE_TURNING_OFF -> {
                    }
                    BluetoothAdapter.STATE_ON -> {
                        operationResult = enable
                        sleepingThread.interrupt()
                    }
                    BluetoothAdapter.STATE_TURNING_ON -> {
                    }
                }
            }
        }
    }

    /**
     * Blocking thread that returns the result of enable/disable bluetooth
     */
    fun process(): Boolean {
        sleepingThread = Thread {
            sleepingThread = Thread.currentThread()
            // Register for broadcasts on BluetoothAdapter state change
            val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
            targetAppContext.registerReceiver(receiver, filter)

            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            val isEnabled = bluetoothAdapter.isEnabled
            if (enable && !isEnabled) {
                bluetoothAdapter.enable()
            } else if (!enable && isEnabled) {
                bluetoothAdapter.disable()
            } else {
                operationResult = true
                return@Thread
            }
            kotlin.runCatching {
                Thread.sleep(5000)
            }
        }
        sleepingThread.start()
        sleepingThread.join()
        targetAppContext.unregisterReceiver(receiver)
        return operationResult ?: false
    }
}
