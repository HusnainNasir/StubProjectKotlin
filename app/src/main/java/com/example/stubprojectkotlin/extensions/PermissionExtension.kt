package com.example.stubprojectkotlin.extensions

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.example.stubprojectkotlin.BuildConfig
import com.example.stubprojectkotlin.R
import com.example.stubprojectkotlin.extensions.Extensions.locationEnabled
import com.example.stubprojectkotlin.utils.AlertDialogs
import com.example.stubprojectkotlin.utils.Constants
import com.qifan.powerpermission.askPermissions
import com.qifan.powerpermission.data.hasAllGranted
import com.qifan.powerpermission.data.hasPermanentDenied
import com.qifan.powerpermission.data.hasRational

object PermissionExtension {


    fun AppCompatActivity.locationPermission(permissionCallback: (Boolean) -> Unit = {}){
        if (locationEnabled()) {
            requestLocationPermission(permissionCallback)
        } else {
            AlertDialogs.showAlertDialog(this, getString(R.string.location_setting),
                getString(R.string.enableLocation), getString(R.string.setting),
                getString(R.string.cancel), false){
                if (it == Constants.CLICK_POSITIVE){
                    permissionCallback(false)
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            }
        }
    }


    private fun AppCompatActivity.requestLocationPermission(permissionCallback: (Boolean) -> Unit = {}){
        askPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) { permissionResult ->
            when {
                permissionResult.hasAllGranted() -> {
                    permissionCallback(true)
                }
                permissionResult.hasRational() -> {
                    permissionCallback(false)
                }
                permissionResult.hasPermanentDenied() -> {
                    permissionCallback(false)
                    appSettingDialog()
                }
            }
        }
    }

    private fun Context.appSettingDialog() {
        AlertDialogs.showAlertDialog(
            this,
            getString(R.string.required_permission),
            getString(R.string.permission_denied_text),
            getString(R.string.setting),
            getString(R.string.cancel),
            false
        ) {
            if (it == Constants.CLICK_POSITIVE) {
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                    )
                )
            }
        }
    }


    fun AppCompatActivity.requestCameraPermission(permissionCallback: (Boolean) -> Unit = {}) {

        askPermissions(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) { permissionResult ->
            when {
                permissionResult.hasAllGranted() -> {
                    permissionCallback(true)
                }
                permissionResult.hasRational() -> {
                    permissionCallback(false)
                }
                permissionResult.hasPermanentDenied() -> {
                    permissionCallback(false)
                    appSettingDialog()
                }
            }
        }
    }

}