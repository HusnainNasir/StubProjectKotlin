package com.example.stubprojectkotlin.callbacks

import com.google.android.gms.maps.model.LatLng

interface LocationPermissionCallback {
    fun locationCallback(currentLatLng: LatLng, fakeLocation: Boolean)
}