package com.troido.bless.app.scan.button_control.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.troido.bless.app.R
import com.troido.bless.app.common.ViewMvpFactory
import com.troido.bless.app.common.views.BaseObservableViewMvp
import com.troido.bless.app.databinding.ActivityDeviceScanBinding
import com.troido.bless.app.model.Device

class DeviceScanViewMvpImpl(
    layoutInflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvpFactory: ViewMvpFactory
) : BaseObservableViewMvp<DeviceScanViewMvp.Listener>(),
    DeviceScanViewMvp,
    DeviceAdapter.Listener {

    private val binding = ActivityDeviceScanBinding.inflate(layoutInflater, parent, false)

    override val rootView: View = binding.root

    private val deviceAdapter: DeviceAdapter = DeviceAdapter(this, viewMvpFactory)

    init {
        binding.deviceListRecyclerView.adapter = deviceAdapter
        binding.deviceListRecyclerView.addItemDecoration(
            DividerItemDecoration(rootView.context, DividerItemDecoration.VERTICAL)
        )
        binding.scanButton.setOnClickListener {
            if (binding.scanButton.text == rootView.context.getString(R.string.start_scan_button_text)) {
                listeners.forEach { it.onStartScanClicked() }
            } else {
                listeners.forEach { it.onStopScanClicked() }
            }
        }
    }

    override fun bindDevices(devices: List<Device>) {
        deviceAdapter.submitList(devices)
    }

    override fun bindScanButtonState(isScanning: Boolean) {
        if (isScanning) {
            binding.scanButton.setText(R.string.stop_scan_button_text)
        } else {
            binding.scanButton.setText(R.string.start_scan_button_text)
        }
    }

    override fun onDeviceClicked(device: Device) {
        listeners.forEach { it.onDeviceClicked(device) }
    }
}