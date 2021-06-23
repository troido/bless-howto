package com.troido.bless.app.deserialization.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter
import com.troido.bless.app.model.Device

class DeviceSpinnerAdapter : SpinnerAdapter, BaseAdapter() {

    private val devices = mutableListOf<Device>()

    fun setDevices(devices: List<Device>) {
        this.devices.clear()
        this.devices.addAll(devices)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return devices.size
    }

    override fun getItem(position: Int): Device {
        return devices[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return if (convertView == null) {
            val viewMvp: DeviceSpinnerViewMvp =
                DeviceSpinnerViewMvpImpl(LayoutInflater.from(parent.context), parent)
            viewMvp.bindDevice(devices[position])
            viewMvp.rootView.tag = viewMvp
            viewMvp.rootView
        } else {
            val viewMvp = convertView.tag as DeviceSpinnerViewMvp
            viewMvp.bindDevice(devices[position])
            convertView
        }
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return if (convertView == null) {
            val viewMvp: DeviceSpinnerDropdownViewMvp =
                DeviceSpinnerDropdownViewMvpImpl(LayoutInflater.from(parent.context), parent)
            viewMvp.bindDevice(devices[position])
            viewMvp.rootView.tag = viewMvp
            viewMvp.rootView
        } else {
            val viewMvp = convertView.tag as DeviceSpinnerDropdownViewMvp
            viewMvp.bindDevice(devices[position])
            convertView
        }
    }
}