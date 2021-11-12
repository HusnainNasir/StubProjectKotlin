package com.example.stubprojectkotlin.ui.main_activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.stubprojectkotlin.BaseActivity
import com.example.stubprojectkotlin.databinding.ActivityMainBinding
import com.example.stubprojectkotlin.db.entites.User
import com.example.stubprojectkotlin.network.MainViewModel
import com.example.stubprojectkotlin.utils.LocationManager
import com.example.stubprojectkotlin.utils.Status.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

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

    private lateinit var locationManager: LocationManager

    private lateinit var mainViewModel: MainViewModel

    override fun created(savedInstance: Bundle?) {

        locationManager = LocationManager(this, true)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        activityMainBinding.helloWorld.setOnClickListener {

            val loginHashMap: HashMap<String, Any> = HashMap()

            loginHashMap["user[email]"] = "wasimamin538@gmail.com"
            loginHashMap["user[password]"] = "123456"
            mainViewModel.login(loginHashMap)
        }

        activityMainBinding.getMalls.setOnClickListener {
//            mainViewModel.getMalls()



            GlobalScope.launch(Dispatchers.Main) {
                val userOne = withContext(Dispatchers.IO) { mainViewModel.getMalls() }

            }
        }

        mainViewModel.getMallsLive().observe(this, {
            when (it.status) {

                SUCCESS -> Log.d(tag, it.data.toString())
                ERROR -> it.message?.let { it1 -> Log.d(tag, it1) }
                LOADING -> TODO()
                INTERNET_CONNECTIVITY -> Toast.makeText(this , it.message , Toast.LENGTH_SHORT).show()
            }
        })



//        GlobalScope.launch {
//            val result = mainViewModel.getUsersFromDB()
//
//            Log.d(tag , result[0].name)
//        }


    }

     private fun showUser(users : List<User>) {

         if (users.isNotEmpty())
           Log.d(tag , users.toString())
    }

}