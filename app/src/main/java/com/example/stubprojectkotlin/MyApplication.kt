package com.example.stubprojectkotlin

import android.content.Context
import androidx.multidex.MultiDexApplication

class MyApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
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