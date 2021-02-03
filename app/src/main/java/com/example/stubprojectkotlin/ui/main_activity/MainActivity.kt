package com.example.stubprojectkotlin.ui.main_activity

import android.os.Bundle
import android.util.Log
import com.example.stubprojectkotlin.BaseActivity
import com.example.stubprojectkotlin.R
import com.example.stubprojectkotlin.callbacks.LocationPermissionCallback
import com.example.stubprojectkotlin.callbacks.PermissionCallback
import com.example.stubprojectkotlin.utils.LocationManager
import com.google.android.gms.maps.model.LatLng

class MainActivity : BaseActivity() {

    override val layoutId: Int
        get() = R.layout.activity_main
    override val tag: String?
        get() = MainActivity::class.simpleName

    private lateinit var locationManager: LocationManager

    override fun created(savedInstance: Bundle?) {

        locationManager = LocationManager(this, true)

        locationPermission(object : PermissionCallback {
            override fun permissionCallback(isPermission: Boolean) {
                if (isPermission) {
                    locationManager.locationRequest(object : LocationPermissionCallback {
                        override fun locationCallback(
                            currentLatLng: LatLng,
                            fakeLocation: Boolean
                        ) {

                           Log.d(tag , currentLatLng.latitude.toString())
                        }
                    })
                }
            }
        })
    }
}