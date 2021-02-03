package com.example.stubprojectkotlin.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Looper
import com.example.stubprojectkotlin.callbacks.LocationPermissionCallback
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng

class LocationManager(activity: Activity, private val removeUpdateLocation: Boolean) {

    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocationLatLng: LatLng? = null
    private lateinit var locationPermissionCallback: LocationPermissionCallback


    @SuppressLint("MissingPermission")
    fun locationRequest(locationPermissionCallback: LocationPermissionCallback) {
        this.locationPermissionCallback = locationPermissionCallback

        buildLocationRequest()
        buildLocationCallBack()
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 4000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 20f
    }

    private fun buildLocationCallBack() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    currentLocationLatLng = LatLng(location.latitude, location.longitude)

                    if (removeUpdateLocation)
                        fusedLocationClient.removeLocationUpdates(locationCallback)

                    locationPermissionCallback.locationCallback(currentLocationLatLng!!, location.isFromMockProvider)
                }
            }
        }
    }

}