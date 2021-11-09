package com.troido.bless.app.bonding

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.troido.bless.BluetoothDevice
import com.troido.bless.app.R

class BondingRecyclerViewAdapter(private val devices: MutableList<BluetoothDevice>, val onItemClicked: (BluetoothDevice) -> Unit) :
    RecyclerView.Adapter<BondingRecyclerViewAdapter.BondingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BondingViewHolder {
        return BondingViewHolder(parent)
    }

    override fun onBindViewHolder(holder: BondingViewHolder, position: Int) {
        val device = devices[position]
        holder.bind(device, onItemClicked)
    }

    override fun getItemCount() = devices.size

    fun addDevices(vararg devices: BluetoothDevice) {
        val callback = object : DiffUtil.Callback() {
            val oldDevices = this@BondingRecyclerViewAdapter.devices

            override fun getOldListSize() = oldDevices.size

            override fun getNewListSize() = devices.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return devices[newItemPosition] == oldDevices[oldItemPosition]
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return devices[newItemPosition].address == oldDevices[oldItemPosition].address
            }
        }

        val result = DiffUtil.calculateDiff(callback, true)
        this.devices.clear()
        this.devices.addAll(devices)

        result.dispatchUpdatesTo(this)
    }

    inner class BondingViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_device, parent, false)
        ) {

        fun bind(device: BluetoothDevice, callback: (BluetoothDevice) -> Unit) {
            itemView.findViewById<TextView>(R.id.device_name_text_view)
                .text = device.name ?: "Unknown"
            itemView.findViewById<TextView>(R.id.device_address_text_view)
                .text = device.address

            itemView.setOnClickListener {
                callback(device)
            }
        }
    }
}
