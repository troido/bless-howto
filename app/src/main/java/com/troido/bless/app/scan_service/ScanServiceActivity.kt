package com.troido.bless.app.scan_service

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.troido.bless.app.R
import com.troido.bless.app.databinding.LayoutScanServiceBinding
import com.troido.bless.app.main.PermissionsActivity
import timber.log.Timber

class ScanServiceActivity : PermissionsActivity(
    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
    R.string.location_permission_rationale
) {

    private lateinit var binding: LayoutScanServiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutScanServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startServiceButton.setOnClickListener {
            Timber.d("Start service button clicked")
            val foreground = ActivityCompat.checkSelfPermission(this,Manifest.permission.FOREGROUND_SERVICE)
            val admin = ActivityCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_ADMIN)
            Timber.d("Answer for foreground is $foreground. for admin is $admin")
            runWithPermissions {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    startForegroundService(Intent(this, ScanService::class.java))
                } else {
                    startService(Intent(this, ScanService::class.java))
                }
            }
        }
        binding.stopServiceButton.setOnClickListener {
            Timber.d("Stop service button clicked")
            stopService(Intent(this, ScanService::class.java))
        }
    }
}
