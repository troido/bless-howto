package com.troido.bless.app.deserialization.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.DividerItemDecoration
import com.troido.bless.app.common.views.BaseObservableViewMvp
import com.troido.bless.app.databinding.LayoutDeserializationBinding
import com.troido.bless.app.deserialization.DeserializationData
import com.troido.bless.app.model.Device

class DeserializationViewMvpImpl(
    layoutInflater: LayoutInflater,
    parent: ViewGroup?
) : DeserializationViewMvp,
    BaseObservableViewMvp<DeserializationViewMvp.Listener>(),
    AdapterView.OnItemSelectedListener {

    private val binding = LayoutDeserializationBinding.inflate(layoutInflater, parent, false)

    override val rootView: View = binding.root

    private val deviceSpinnerAdapter = DeviceSpinnerAdapter()

    private val deserializationDataAdapter = DeserializationDataAdapter()

    init {
        binding.scanStartButton.setOnClickListener {
            listeners.forEach { it.onStartScanButtonClicked() }
        }
        binding.scanStopButton.setOnClickListener {
            listeners.forEach { it.onStopScanButtonClicked() }
        }
        binding.devicesSpinner.adapter = deviceSpinnerAdapter
        binding.devicesSpinner.onItemSelectedListener = this
        binding.deserializationDataRecyclerView.adapter = deserializationDataAdapter
        binding.deserializationDataRecyclerView.addItemDecoration(
            DividerItemDecoration(rootView.context, DividerItemDecoration.VERTICAL)
        )

        binding.devicesSpinner.visibility = View.INVISIBLE
        binding.noDeviceDeserializedTextView.visibility = View.VISIBLE
    }

    override fun bindScanningState(isScanning: Boolean) {
        if (isScanning) {
            binding.scanStartButton.visibility = View.GONE
            binding.scanStopButton.visibility = View.VISIBLE
        } else {
            binding.scanStartButton.visibility = View.VISIBLE
            binding.scanStopButton.visibility = View.GONE
        }
    }

    override fun bindDevices(devices: List<Device>) {
        binding.devicesSpinner.visibility = View.VISIBLE
        binding.noDeviceDeserializedTextView.visibility = View.INVISIBLE
        deviceSpinnerAdapter.setDevices(devices)
    }

    override fun bindDeserializedData(deserializationData: DeserializationData?) {
        val data = deserializationData?.data ?: emptyList()
        if (data.isEmpty()) {
            binding.noParametersTextView.visibility = View.VISIBLE
        } else binding.noParametersTextView.visibility = View.INVISIBLE
        deserializationDataAdapter.setDeserializationData(data)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        binding.noDeviceSelectedTextView.visibility = View.INVISIBLE
        val device = deviceSpinnerAdapter.getItem(position)
        listeners.forEach { it.onDeviceSelected(device) }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Do nothing
    }
}
