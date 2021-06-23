package com.troido.bless.app.scan.button_control.views

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.troido.bless.app.common.ViewMvpFactory
import com.troido.bless.app.model.Device

class DeviceAdapter(
    private val listener: Listener,
    private val viewMvpFactory: ViewMvpFactory
) : ListAdapter<Device, DeviceAdapter.ViewHolder>(DeviceDiffUtil()),
    DeviceListItemViewMvp.Listener {

    interface Listener {

        fun onDeviceClicked(device: Device)
    }

    class ViewHolder(
        internal val viewMvp: DeviceListItemViewMvp
    ) : RecyclerView.ViewHolder(viewMvp.rootView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewMvp = viewMvpFactory.getDeviceListItemViewMvp(parent)
        viewMvp.registerListener(this)
        return ViewHolder(viewMvp)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewMvp.bindDevice(getItem(position))
    }

    override fun onDeviceClicked(device: Device) {
        listener.onDeviceClicked(device)
    }

    class DeviceDiffUtil : DiffUtil.ItemCallback<Device>() {

        override fun areItemsTheSame(oldItem: Device, newItem: Device): Boolean {
            return oldItem.address == newItem.address
        }

        override fun areContentsTheSame(oldItem: Device, newItem: Device): Boolean {
            return oldItem == newItem
        }
    }
}