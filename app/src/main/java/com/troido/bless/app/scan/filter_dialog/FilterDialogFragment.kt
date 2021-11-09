package com.troido.bless.app.scan.filter_dialog

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.troido.bless.app.R
import com.troido.bless.app.common.extensions.copyList
import com.troido.bless.app.common.util.MacAddressFormatTextWatcher
import com.troido.bless.app.databinding.DialogFilterFragmentBinding
import com.troido.bless.app.model.ScanConfig
import com.troido.bless.scan.ScanFilter
import com.troido.bless.scan.ScanMode
import com.troido.bless.scan.ScanSettings

class FilterDialogFragment : DialogFragment() {

    private lateinit var rootViewBinding: DialogFilterFragmentBinding

    private lateinit var macAddressTextWatcher: MacAddressFormatTextWatcher
    private val filterTargetMacAddresses = mutableSetOf<String>()

    private lateinit var saveScanConfigCallback: SaveScanConfigCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootViewBinding = DialogFilterFragmentBinding.inflate(inflater, container, false)
        setResizeDialogWhenKeyboardAppears()
        return rootViewBinding.root
    }

    private fun setResizeDialogWhenKeyboardAppears() {
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleMacAddressInput()
        handleMacAddressList()

        rootViewBinding.cancelButton.setOnClickListener {
            dismiss()
        }

        rootViewBinding.startScanButton.setOnClickListener {
            if (this::saveScanConfigCallback.isInitialized) {
                val scanConfig = ScanConfig(
                    this.filterTargetMacAddresses,
                    getReportDelay(),
                    getSelectedScanMode()
                )
                saveScanConfigCallback.onSaveScanConfig(scanConfig)
            }
            dismiss()
        }
    }

    private fun handleMacAddressInput() {
        val macAddressInputEditText = rootViewBinding.macAddressInputEditText
        macAddressTextWatcher = MacAddressFormatTextWatcher.attach(
            macAddressInputEditText
        )

        rootViewBinding.addMacFilterButton.setOnClickListener {
            macAddressInputEditText.text.takeIf { BluetoothAdapter.checkBluetoothAddress(it.toString()) }
                ?.let {
                    filterTargetMacAddresses.add(it.toString())
                    (rootViewBinding.filterByMacAddressListView.adapter as MacAddressListAdapter)
                        .updateData(filterTargetMacAddresses.copyList())

                    macAddressInputEditText.setText("")
                } ?: macAddressInputEditText.setError(getString(R.string.invalid_mac_address))
        }
    }

    private fun handleMacAddressList() {
        val listView = rootViewBinding.filterByMacAddressListView
        listView.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        )

        listView.adapter = MacAddressListAdapter {
            removeAddressAndUpdateListView(it)
        }
    }

    private fun removeAddressAndUpdateListView(it: String) {
        if (filterTargetMacAddresses.remove(it)) {
            (rootViewBinding.filterByMacAddressListView.adapter as MacAddressListAdapter)
                .updateData(filterTargetMacAddresses.copyList())
        }
    }

    private fun getSelectedScanMode(): String {
        val radioGroup = rootViewBinding.scanModeOptionsRadioGroup
        val selectedRadioButtonId = radioGroup.checkedRadioButtonId
        val selectedView = radioGroup.findViewById<RadioButton>(selectedRadioButtonId)

        return selectedView.text.toString()
    }

    private fun getReportDelay(): Long {
        var result: Long
        try {
            result = (rootViewBinding.reportDelayEditText.text).toString().toLong()
        } catch (exception: NumberFormatException) {
            result = 0L
        }

        return result
    }

    fun show(manager: FragmentManager, saveScanConfigCallback: SaveScanConfigCallback) {
        super.show(manager, TAG)
        this.saveScanConfigCallback = saveScanConfigCallback
    }

    companion object {
        private const val TAG = "FilterDialogFragment"

        fun newInstance(): FilterDialogFragment {
            return FilterDialogFragment()
        }

        fun getScanFilterAndSettingsPair(
            context: Context,
            scanConfig: ScanConfig?
        ): Pair<ScanSettings?, ScanFilter?> {
            var scanSettings: ScanSettings? = null
            var scanFilter: ScanFilter? = null

            scanConfig?.let {
                val scanMode = when (it.scanMode) {
                    context.getString(R.string.scan_mode_low_latency) -> ScanMode.LOW_LATENCY
                    context.getString(R.string.scan_mode_low_power) -> ScanMode.LOW_POWER
                    else -> ScanMode.BALANCED
                }

                scanSettings = ScanSettings.Builder()
                    .reportDelay(it.reportDelay)
                    .scanMode(scanMode)
                    .build()

                scanFilter = ScanFilter.Builder()
                    .setDeviceAddresses(scanConfig.filterByMacAddresses)
                    .build()
            }

            return Pair(scanSettings, scanFilter)
        }
    }
}
