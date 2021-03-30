package com.example.stubprojectkotlin.ui.main_activity

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.stubprojectkotlin.BaseActivity
import com.example.stubprojectkotlin.R
import com.example.stubprojectkotlin.databinding.ActivityMainBinding
import com.example.stubprojectkotlin.network.MainViewModel
import com.example.stubprojectkotlin.utils.LocationManager
import com.example.stubprojectkotlin.utils.Status.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    override val layoutId: Int
        get() = R.layout.activity_main
    override val tag: String?
        get() = MainActivity::class.simpleName

    private lateinit var binding: ActivityMainBinding

    private lateinit var locationManager: LocationManager

    private lateinit var mainViewModel: MainViewModel

    override fun created(savedInstance: Bundle?) {

        binding = ActivityMainBinding.inflate(layoutInflater)

        locationManager = LocationManager(this, true)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        helloWorld.setOnClickListener {

            val loginHashMap: HashMap<String , Any> = HashMap()

            loginHashMap["user[email]"] = "wasimamin538@gmail.com"
            loginHashMap["user[password]"] = "123456"
            mainViewModel.login(loginHashMap)
        }

        getMalls.setOnClickListener {
            mainViewModel.getMalls()
        }

        mainViewModel.getUsers().observe(this , {
            when(it.status){

                SUCCESS -> Log.d(tag , it.data.toString())
                ERROR -> it.message?.let { it1 -> Log.d(tag , it1) }
                LOADING -> TODO()
            }
        })


    }

}