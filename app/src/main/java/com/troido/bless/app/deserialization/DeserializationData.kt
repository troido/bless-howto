package com.troido.bless.app.deserialization

import com.troido.bless.app.model.Device

class DeserializationData(
    val device: Device,
    val data: List<Pair<String, String>>
)