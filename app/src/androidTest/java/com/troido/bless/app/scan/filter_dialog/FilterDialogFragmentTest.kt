package com.troido.bless.app.scan.filter_dialog

import androidx.fragment.app.testing.launchFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.RequiresDevice
import com.schibsted.spain.barista.assertion.BaristaErrorAssertions.assertErrorDisplayed
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertListItemCount
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItemChild
import com.schibsted.spain.barista.internal.failurehandler.BaristaException
import com.troido.bless.app.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@RequiresDevice
class FilterDialogFragmentTest {

    @Test
    fun testMainFunctionality() {
        launchFragment<FilterDialogFragment>(themeResId = R.style.AppThemeWithActionBar)

        pressAddOnAnEmptyListShouldShowError()
        enterInvalidMacAddressShouldShowError()
        enterTheSameMacAddressTwiceShouldDisplayOnlyOne()
        deleteDisplayedItemAndCheckThatRecyclerViewIsEmpty()
    }

    private fun deleteDisplayedItemAndCheckThatRecyclerViewIsEmpty() {
        clickListItemChild(R.id.filter_by_mac_address_list_view, 0, R.id.delete_mac_address_icon)
        try {
            assertListItemCount(R.id.filter_by_mac_address_list_view, 0)
        } catch (e: BaristaException) {
            Thread.sleep(5000)
            assertListItemCount(R.id.filter_by_mac_address_list_view, 0)
        }
    }

    private fun enterTheSameMacAddressTwiceShouldDisplayOnlyOne() {
        writeTo(R.id.mac_address_input_edit_text, "AA:BB:CC:DD:FF:AA")
        clickOn(R.id.add_mac_filter_button)
        writeTo(R.id.mac_address_input_edit_text, "AA:BB:CC:DD:FF:AA")
        clickOn(R.id.add_mac_filter_button)
        assertListItemCount(R.id.filter_by_mac_address_list_view, 1)
    }

    private fun enterInvalidMacAddressShouldShowError() {
        writeTo(R.id.mac_address_input_edit_text, "AA:BB")
        clickOn(R.id.add_mac_filter_button)
        assertErrorDisplayed(R.id.mac_address_input_edit_text, R.string.invalid_mac_address)
    }

    private fun pressAddOnAnEmptyListShouldShowError() {
        clickOn(R.id.add_mac_filter_button)
        assertErrorDisplayed(R.id.mac_address_input_edit_text, R.string.invalid_mac_address)
    }
}
