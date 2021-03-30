package com.example.stubprojectkotlin

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.stubprojectkotlin.callbacks.OnClickCallback
import com.example.stubprojectkotlin.callbacks.PermissionCallback
import com.example.stubprojectkotlin.utils.AlertDialogs
import com.example.stubprojectkotlin.utils.Constants
import com.example.stubprojectkotlin.utils.PreferenceHelper
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks
import pub.devrel.easypermissions.PermissionRequest
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity(), PermissionCallbacks {
    // Getters/Setters
    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    @Inject
    lateinit var gSON: Gson

    private var permissionCallback: PermissionCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutId != 0) {
            setContentView(layoutId)
            created(savedInstanceState)
        }
    }

    // Permissions Camera , Location
    private fun locationEnabled(): Boolean {
        val lm = getSystemService(LOCATION_SERVICE) as LocationManager
        var gpsEnabled = false
        var networkEnabled = false
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return gpsEnabled || networkEnabled
    }

    fun locationPermission(permissionCallback: PermissionCallback) {
        this.permissionCallback = permissionCallback
        if (locationEnabled()) {
            requestLocationPermission()
        } else {
            AlertDialogs.showAlertDialog(this, getString(R.string.location_setting),
                getString(R.string.enableLocation), getString(R.string.setting),
                getString(R.string.cancel), false, object : OnClickCallback {
                    override fun onclick(obj: Any?) {
                        if (obj as Int == Constants.CLICK_POSITIVE) {
                            startActivityForResult(
                                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                                AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE
                            )
                        }
                    }

                })
        }
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(Constants.FINE_AND_COURSE_LOCATION_PERMISSION)
    fun requestLocationPermission() {
        val perms = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            // Already have permission, do the thing
            permissionCallback!!.permissionCallback(true)
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(
                    this,
                    Constants.FINE_AND_COURSE_LOCATION_PERMISSION,
                    *perms
                )
                    .setTheme(R.style.AlertDialogCustom)
                    .build()
            )
        }
    }

    fun cameraPermission(permissionCallback: PermissionCallback?) {
        this.permissionCallback = permissionCallback
        requestCameraPermission()
    }

    @AfterPermissionGranted(Constants.CAMERA_AND_READ_AND_WRITE_PERMISSION)
    fun requestCameraPermission() {
        val perms = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            // Already have permission, do the thing
            permissionCallback!!.permissionCallback(true)
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(
                    this,
                    Constants.CAMERA_AND_READ_AND_WRITE_PERMISSION,
                    *perms
                )
                    .setTheme(R.style.AlertDialogCustom)
                    .build()
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, list: List<String>) {
        // Some permissions have been granted
        if (requestCode == Constants.CAMERA_AND_READ_AND_WRITE_PERMISSION) permissionCallback!!.permissionCallback(
            true
        ) else if (requestCode == Constants.FINE_AND_COURSE_LOCATION_PERMISSION) permissionCallback!!.permissionCallback(
            true
        )
    }

    override fun onPermissionsDenied(requestCode: Int, list: List<String>) {
        // Some permissions have been denied
        if (requestCode == Constants.CAMERA_AND_READ_AND_WRITE_PERMISSION) permissionCallback!!.permissionCallback(
            false
        ) else if (requestCode == Constants.FINE_AND_COURSE_LOCATION_PERMISSION) permissionCallback!!.permissionCallback(
            false
        )
        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
        }
    }

    // setToolbar
    fun setToolbar(title: String?) {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // Activity Navigation
    fun activityNavigation(
        context: Context?, activityClass: Class<*>?, isFinish: Boolean,
        dataName: List<String>, dataValues: List<Any>
    ) {

        val activityIntent = Intent(context, activityClass)
        val incrementData = 0
        for (intentValue in dataName) {
            activityIntent.putExtra(intentValue, gSON?.toJson(dataValues[incrementData]))
        }
        startActivity(activityIntent)
        if (isFinish) finish()
    }

    // Abstract Method
    protected abstract val layoutId: Int
    protected abstract val tag: String?
    protected abstract fun created(savedInstance: Bundle?)
}