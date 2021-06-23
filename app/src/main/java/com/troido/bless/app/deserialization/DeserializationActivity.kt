package com.troido.bless.app.deserialization

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import com.troido.bless.app.R
import com.troido.bless.app.deserialization.views.DeserializationViewMvp
import com.troido.bless.app.deserialization.views.DeserializationViewMvpImpl
import com.troido.bless.app.main.PermissionsActivity
import com.troido.bless.app.model.Device
import org.koin.android.ext.android.inject

class DeserializationActivity : PermissionsActivity(
    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
    R.string.location_permission_rationale
), DeserializationViewMvp.Listener {

    private lateinit var viewMvp: DeserializationViewMvp

    private val deserializationViewModel: DeserializationViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewMvp = DeserializationViewMvpImpl(LayoutInflater.from(this), null)
        viewMvp.registerListener(this)
        setContentView(viewMvp.rootView)

        deserializationViewModel.devicesLiveData.observe(this, {
            viewMvp.bindDevices(it)
        })
        deserializationViewModel.isScanningLiveData.observe(this, {
            viewMvp.bindScanningState(it)
        })
        deserializationViewModel.deserializedDataLiveData.observe(this, {
            viewMvp.bindDeserializedData(it)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewMvp.unregisterListener(this)
    }

    /**
     * [PermissionsActivity] handles permissions automatically, so we suppress the warning here
     */
    @SuppressLint("MissingPermission")
    override fun onStartScanButtonClicked() {
        runWithPermissions {
            deserializationViewModel.startScan()
        }
    }

    override fun onStopScanButtonClicked() {
        deserializationViewModel.stopScan()
    }

    override fun onDeviceSelected(device: Device) {
        deserializationViewModel.setSelectedDevice(device)
    }
}