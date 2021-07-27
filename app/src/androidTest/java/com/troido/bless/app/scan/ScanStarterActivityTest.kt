package com.troido.bless.app.scan

import android.content.res.Resources
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.RequiresDevice
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo
import com.troido.bless.app.R
import com.troido.bless.app.utils.enableTurnOnBluetoothSystemDialogIfAppears
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep


@RunWith(AndroidJUnit4::class)
@RequiresDevice
class ScanStarterActivityTest {
    @get:Rule
    var activityRule: ActivityScenarioRule<ScanStarterActivity> =
        ActivityScenarioRule(ScanStarterActivity::class.java)

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    @Test
    fun testAllButtonsAndActivityTransitions() {
        goToScanActivity()
        enableTurnOnBluetoothSystemDialogIfAppears()
        checkThatScanActivityIsVisible()
        sleep(200)
        pressBack()

        goToScanWithFilterActivity()
        writeTo(R.id.report_delay_edit_text, "100")
        clickOn(R.id.start_scan_button)
        checkThatScanActivityIsVisible()
        sleep(200)
        pressBack()

        goToScanActivityWithAppThemeActivity()
        checkThatActionBarIsVisible()
        sleep(200)
        pressBack()

        goToScanWithCustomAdapterActivity()
        checkThatScanActivityIsVisible()
        sleep(200)
        pressBack()

        goToStartScanWithButtonControlActivity()
        onView(withId(R.id.scan_button)).perform(click())
        sleep(200)
        pressBack()

        goToStartDeviceCounterActivity()
        checkThatDeviceCounterAcitivtyVisible()
        sleep(200)
        pressBack()
    }

    private fun goToScanWithFilterActivity() {
        clickOn(R.id.start_bless_scan_with_filter)
    }

    private fun checkThatDeviceCounterAcitivtyVisible() {
        onView(withId(R.id.count)).check(matches(isDisplayed()))
    }

    private fun goToStartDeviceCounterActivity() {
        performClick(R.id.start_device_counter)
    }

    private fun goToStartScanWithButtonControlActivity() {
        performClick(R.id.start_scan_with_button_control)
    }

    private fun checkThatScanActivityIsVisible() {
        onView(withId(R.id.is_scanning_indicator)).check(matches(isDisplayed()))
    }

    private fun goToScanWithCustomAdapterActivity() {
        performClick(R.id.start_scan_with_custom_adapter)
    }

    private fun checkThatActionBarIsVisible() {
        val resources: Resources = getInstrumentation().targetContext.resources
        val packageName = getInstrumentation().targetContext.packageName
        val actionBarId: Int = resources.getIdentifier("action_bar_container", "id", packageName)

        onView(withId(actionBarId))
            .check(matches(isDisplayed()))
    }

    private fun goToScanActivityWithAppThemeActivity() {
        performClick(R.id.start_bless_scan_with_app_theme)
    }

    private fun goToScanActivity() {
        performClick(R.id.start_bless_scan)
    }

    private fun performClick(@IdRes idRes: Int) {
        onView(withId(idRes)).perform(scrollTo(), click())
    }
}