package com.example.stubprojectkotlin.ui.main_activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.stubprojectkotlin.BaseActivity
import com.example.stubprojectkotlin.data_layer.MainViewModel
import com.example.stubprojectkotlin.databinding.ActivityMainBinding
import com.example.stubprojectkotlin.utils.LocationManager
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding

    override val layoutId: View
        get() {
            activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
            return activityMainBinding.root
        }
    override val tag: String?
        get() = MainActivity::class.simpleName

    private lateinit var mainViewModel: MainViewModel

    override fun created(savedInstance: Bundle?) {

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        activityMainBinding.helloWorld.setOnClickListener {

            val loginHashMap: HashMap<String, Any> = HashMap()

            loginHashMap["user[email]"] = "wasimamin538@gmail.com"
            loginHashMap["user[password]"] = "123456"
            mainViewModel.login(loginHashMap)
        }

    }


}