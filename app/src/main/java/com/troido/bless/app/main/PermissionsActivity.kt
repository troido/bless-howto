package com.troido.bless.app.main

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.troido.bless.app.R
import com.troido.bless.ui.scan.view.DeniedRationaleDialogBuilder
import timber.log.Timber

/**
 * Activity that handles permissions. Permissions can be pass to the constructor or directly to the
 * [runWithPermissions] function. If permissions is not passt to the function then permissions from
 * the constructor will take place. The same with [rationaleMsgRes]. But if [rationaleMsgRes] is null then
 * [R.string.permission_rationale_default] message will be used.
 */
abstract class PermissionsActivity(
    val permissions: Array<String>,
    @StringRes val rationaleMsgRes: Int = R.string.permission_rationale_default,
) : AppCompatActivity() {

    /**
     * Function that invokes when permissions are granted
     */
    private var permissionGrantedFunction: (() -> Unit)? = null

    private lateinit var rationaleDialog: AlertDialog
    private lateinit var rationaleDeniedPermissionsDialog: AlertDialog

    /**
     * Run provided code with the permissions. If the permissions are not granted then activity will handle
     * permissions flow and invoke this code after the user grants the permissions.
     */
    fun runWithPermissions(
        permissions: Array<out String> = this.permissions,
        code: () -> Unit
    ) {
        if (permissions.isGranted()) {
            code()
        } else {
            this.permissionGrantedFunction = code
            requestPermission.launch(permissions)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rationaleDialog = AlertDialog.Builder(this)
            .setMessage(rationaleMsgRes)
            .setPositiveButton(getString(com.troido.bless.ui.R.string.ok)) { _, _ ->
                requestPermission.launch(permissions)
            }
            .setNegativeButton(getString(com.troido.bless.ui.R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .create()

        rationaleDeniedPermissionsDialog = DeniedRationaleDialogBuilder.createDefault(
            this,
            { _, _ ->
                requestOpenSettings.launch(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", packageName, null)
                    }
                )
            },
            { dialog, _ ->
                dialog.dismiss()
            }
        )
    }

    /**
     * Request for granting permissions in permissions dialog
     */
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->
            if (isGranted.values.all { it }) {
                permissionGrantedFunction?.invoke().also {
                    permissionGrantedFunction = null
                }
                return@registerForActivityResult
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (permissions.any { !shouldShowRequestPermissionRationale(it) }) {
                    rationaleDeniedPermissionsDialog.show()
                } else {
                    rationaleDialog.show()
                }
            }
        }

    /**
     * Request for opening app settings and handling permissions after return
     */
    private val requestOpenSettings =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Timber.d("User returned form settings check permissions again")
            if (permissions.isGranted()) {
                Timber.d("Permissions enabled")
                permissionGrantedFunction?.invoke().also {
                    permissionGrantedFunction = null
                }
            } else {
                Timber.d("permissions are still not enabled. returning with error")
                permissionGrantedFunction = null
            }
        }

    /**
     * Check if all of the permissions are granted
     */
    private fun Array<out String>.isGranted(): Boolean {
        return this.all {
            ContextCompat.checkSelfPermission(
                this@PermissionsActivity,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}
