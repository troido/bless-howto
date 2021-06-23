package com.troido.bless.app.scan.filter_dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.troido.bless.app.databinding.ViewHolderMacAddressBinding

class MacAddressListAdapter(private val deleteListener: (String) -> Unit) :
    ListAdapter<String, MacAddressListAdapter.MacAddressViewHolder>(
        MacAddressDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MacAddressViewHolder {
        return MacAddressViewHolder(
            ViewHolderMacAddressBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MacAddressViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun updateData(data: List<String>) {
        submitList(data)
    }

    inner class MacAddressViewHolder(
        private val viewBinding: ViewHolderMacAddressBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(address: String) {
            viewBinding.macAddressTextView.text = address
            viewBinding.deleteMacAddressIcon.setOnClickListener {
                deleteListener(address)
            }
        }
    }

    private class MacAddressDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem === newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    }
}