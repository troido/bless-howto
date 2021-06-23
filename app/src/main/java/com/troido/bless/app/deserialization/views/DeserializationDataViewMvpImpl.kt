package com.troido.bless.app.deserialization.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.troido.bless.app.databinding.ItemDeserializationDataBinding

class DeserializationDataViewMvpImpl(
    layoutInflater: LayoutInflater,
    parent: ViewGroup?
) : DeserializationDataViewMvp {

    private val binding = ItemDeserializationDataBinding.inflate(layoutInflater, parent, false)

    override val rootView: View = binding.root

    override fun bindDeserializationData(name: String, value: String) {
        binding.nameTextView.text = name
        binding.valueTextView.text = value
    }
}