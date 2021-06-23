package com.troido.bless.app.deserialization.views

import com.troido.bless.app.common.views.ViewMvp

interface DeserializationDataViewMvp : ViewMvp {

    fun bindDeserializationData(name: String, value: String)
}