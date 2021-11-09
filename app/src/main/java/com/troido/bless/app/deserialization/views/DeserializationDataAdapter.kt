package com.troido.bless.app.deserialization.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class DeserializationDataAdapter :
    ListAdapter<Pair<String, String>, DeserializationDataAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<Pair<String, String>>() {
            override fun areItemsTheSame(
                oldItem: Pair<String, String>,
                newItem: Pair<String, String>
            ): Boolean {
                return oldItem.first == newItem.first
            }

            override fun areContentsTheSame(
                oldItem: Pair<String, String>,
                newItem: Pair<String, String>
            ): Boolean {
                return oldItem == newItem
            }
        }
    ) {

    fun setDeserializationData(data: List<Pair<String, String>>) {
        submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewMvp = DeserializationDataViewMvpImpl(LayoutInflater.from(parent.context), parent)
        return ViewHolder(viewMvp)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val deserializationData = getItem(position)
        holder.view.bindDeserializationData(deserializationData.first, deserializationData.second)
    }

    class ViewHolder(
        val view: DeserializationDataViewMvp
    ) : RecyclerView.ViewHolder(view.rootView)
}
