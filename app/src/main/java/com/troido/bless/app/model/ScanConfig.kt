package com.troido.bless.app.model

data class ScanConfig(
    val filterByMacAddresses: Set<String>,
    val reportDelay: Long,
    val scanMode: String
)
