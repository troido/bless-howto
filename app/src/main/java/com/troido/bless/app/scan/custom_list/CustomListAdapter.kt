package com.troido.bless.app.scan.custom_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.troido.bless.BluetoothDevice
import com.troido.bless.app.databinding.CustomViewHolderBinding
import com.troido.bless.scan.ScanResult

class CustomListAdapter(private val onDeviceSelected: (BluetoothDevice) -> Unit) :
    ListAdapter<ScanResult, CustomListViewHolder>(CustomItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListViewHolder {
        val binding =
            CustomViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomListViewHolder, position: Int) {
        val scanResult = getItem(position)
        holder.binding.name.text = scanResult.device.name
        // This is the ConstraintLayout. We have set it to clickable and focusable.
        // The background attribute will give us the clicking animation.
        holder.itemView.setOnClickListener { onDeviceSelected(scanResult.device) }
    }

    class CustomItemCallback : DiffUtil.ItemCallback<ScanResult>() {
        override fun areItemsTheSame(oldItem: ScanResult, newItem: ScanResult): Boolean {
            // The Bluetooth Device Address is unique so we can use it as an ID
            return oldItem.device.address == newItem.device.address
        }

        override fun areContentsTheSame(oldItem: ScanResult, newItem: ScanResult): Boolean {
            // Since the ListItem only shows the name we are only interested in updates on the name.
            return oldItem.device.name == newItem.device.name
        }
    }
}