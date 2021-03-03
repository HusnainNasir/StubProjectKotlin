package com.example.stubprojectkotlin

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.example.stubprojectkotlin.utils.NetworkManager

class MyApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        NetworkManager.getInstance(application = this)
    }

    companion object {
        private var instance: MyApplication? = null
        fun getInstance(): MyApplication {
            return if (instance == null) MyApplication() else instance!!
        }

        @JvmStatic
        val context: Context
            get() = instance!!.applicationContext
    }
}