package com.example.stubprojectkotlin.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NetworkManager(application: Application) {

    private var networkRequest: NetworkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()

    private val connectivityManager: ConnectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val isNetworkState: LiveData<Boolean>
        get() {
            val isNetwork = MutableLiveData<Boolean>()
            connectivityManager.registerNetworkCallback(
                networkRequest,
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        isNetwork.postValue(true)
                    }

                    override fun onLosing(network: Network, maxMsToLive: Int) {
                        super.onLosing(network, maxMsToLive)
                        isNetwork.postValue(false)
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        isNetwork.postValue(false)
                    }

                    override fun onUnavailable() {
                        super.onUnavailable()
                        isNetwork.postValue(false)
                    }
                })
            return isNetwork
        }
    val isConnected: Boolean
        get() {
            val capability =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            return capability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        }

    val isWorking: Boolean
        get() = try {
            val command = "ping -c 1 google.com"
            Runtime.getRuntime().exec(command).waitFor() == 0
        } catch (e: Exception) {
            false
        }

    companion object {
        private var instance: NetworkManager? = null

        fun getInstance(application: Application): NetworkManager {
            return if (instance == null) NetworkManager(application) else instance!!
        }
    }

}
