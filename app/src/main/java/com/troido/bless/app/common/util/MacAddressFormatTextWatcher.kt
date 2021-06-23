package com.troido.bless.app.common.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * TextWatcher class that will automatically format and cap the MAC Address
 *
 * @param targetEditText EditText to which it should attach.
 */
class MacAddressFormatTextWatcher(
    private val targetEditText: EditText,
) : TextWatcher {

    /**
     * Variable with public getter to check if the Watcher has been removed from the target EditText
     */
    var isRemovedFromTarget: Boolean = false
        private set

    init {
            targetEditText.addTextChangedListener(this)
            isRemovedFromTarget = false
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        /* Do Nothing */
    }

    override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
        charSequence?.let {
            var result = removeExcludedChars(it.toString())
            result = formatMacAddress(result)

            targetEditText.removeTextChangedListener(this)
            targetEditText.setText(result.toUpperCase())

            targetEditText.setSelection(result.length)

            targetEditText.addTextChangedListener(this)
        }
    }

    override fun afterTextChanged(s: Editable?) {
        /* Do nothing */
    }

    /**
     * Method to add this instance to the passed EditText, only if it wasn't added.
     */
    fun addToEditText() {
        if (isRemovedFromTarget) {
            targetEditText.addTextChangedListener(this)
            isRemovedFromTarget = false
        }
    }

    /**
     * Method to remove itself from the listener if it was there before.
     */
    fun removeFromEditText() {
        if (isRemovedFromTarget.not()) {
            targetEditText.removeTextChangedListener(this)
            isRemovedFromTarget = true
        }
    }

    companion object {

        fun attach(targetEditText: EditText) : MacAddressFormatTextWatcher {
            return MacAddressFormatTextWatcher(targetEditText)
        }

        @JvmStatic
        private fun removeExcludedChars(mac: String): String {
            return mac.replace(Regex("[^A-Fa-f0-9]"), "")
        }

        @JvmStatic
        private fun formatMacAddress(mac: String): String {
            val stringWithColons = mac.chunked(2)
                .map { chunk -> "$chunk:" }
                .take(6)
                .fold("") { item, accumulator ->  item + accumulator }

            return if (stringWithColons.endsWith(":"))
                stringWithColons.substring(0, (stringWithColons.length - 1))
            else
                stringWithColons
        }
    }
}