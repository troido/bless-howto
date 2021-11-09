package com.troido.bless.app.connect_service

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.RequiresDevice
import androidx.test.rule.GrantPermissionRule
import com.troido.bless.app.R
import com.troido.bless.app.utils.enableTurnOnBluetoothSystemDialogIfAppears
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@RequiresDevice
class ConnectServiceActivityTest {
    @get:Rule
    var activityRule: ActivityScenarioRule<ConnectServiceActivity> =
        ActivityScenarioRule(ConnectServiceActivity::class.java)

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    @Test
    fun testThatActivityIsRunningAndCanScan() {
        onView(withId(R.id.start_service_connect_button)).perform(click())
        enableTurnOnBluetoothSystemDialogIfAppears()
        Thread.sleep(500)
        pressBack()
    }
}
