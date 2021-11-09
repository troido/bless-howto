package com.troido.bless.app.deserialization

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import com.troido.bless.app.R
import com.troido.bless.app.common.extensions.showToast
import com.troido.bless.app.deserialization.views.DeserializationViewMvp
import com.troido.bless.app.deserialization.views.DeserializationViewMvpImpl
import com.troido.bless.app.main.PermissionsActivity
import com.troido.bless.app.model.Device
import com.troido.bless.ui.util.Permissions
import org.koin.android.ext.android.inject

class DeserializationActivity :
    PermissionsActivity(
        Permissions.getBluetoothPermissions(),
        R.string.location_permission_rationale
    ),
    DeserializationViewMvp.Listener {

    private lateinit var viewMvp: DeserializationViewMvp

    private val deserializationViewModel: DeserializationViewModel by inject()

    private val requestBluetooth =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                showToast(getString(R.string.bluetooth_enabled))
                onStartScanButtonClicked()
            } else showToast(getString(R.string.bluetooth_disabled))
        }

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
            if (!deserializationViewModel.startScan()) {
                requestBluetooth.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            }
        }
    }

    override fun onStopScanButtonClicked() {
        deserializationViewModel.stopScan()
    }

    override fun onDeviceSelected(device: Device) {
        deserializationViewModel.setSelectedDevice(device)
    }
}
