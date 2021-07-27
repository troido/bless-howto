package com.troido.bless.app.base

import androidx.test.platform.app.InstrumentationRegistry
import com.troido.bless.app.utils.BluetoothStateChanger
import org.junit.Before

/**
 * Base test class that enables bluetooth before any of the test methods will be executed
 */
abstract class EnabledBluetoothTestCase {
    companion object{
        private var testClass: Class<out EnabledBluetoothTestCase?>? = null

    }

    @Before
    fun setUp() {
        println("Before in base test class")

        if (this.javaClass.equals(testClass)) {
            return;
        }

        val bluetoothStateChanger =
            BluetoothStateChanger(true, InstrumentationRegistry.getInstrumentation().targetContext)
        bluetoothStateChanger.process()

        testClass = this.javaClass
    }
}