package com.troido.bless.app.di

import android.bluetooth.BluetoothAdapter
import android.view.LayoutInflater
import com.troido.bless.Bless
import com.troido.bless.comm.scan.deserialization.*
import com.troido.bless.app.bonding.BondingViewModel
import com.troido.bless.app.common.ViewMvpFactory
import com.troido.bless.app.deserialization.DeserializationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    factory { (inflater: LayoutInflater) -> ViewMvpFactory(inflater) }

    single { Bless.bleScanner }

    single { BluetoothAdapter.getDefaultAdapter() }

    viewModel { DeserializationViewModel(get(), get()) }

    single { BlessCommDeserializer(get(named("formats"))) }

    single(named("formats")) {
        listOf(
            CommFormat(
                id = "test-format",
                name = "test-format",
                requiredBytes = listOf(
                    RequiredByte(2, 0x59),
                    RequiredByte(3, 0x00),
                    RequiredByte(4, 0x69),
                    RequiredByte(5, 0x08),
                    RequiredByte(6, 0x81.toByte()),
                    RequiredByte(7, 0x01)
                ),
                dataFormat = listOf(
                    DataFormat("Temperature", 8, 12, true, DataType.FLOAT, null)
                )
            )
        )
    }

    viewModel { BondingViewModel() }
}