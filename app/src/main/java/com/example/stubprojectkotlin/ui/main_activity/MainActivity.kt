package com.example.stubprojectkotlin.ui.main_activity

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.stubprojectkotlin.BaseActivity
import com.example.stubprojectkotlin.databinding.ActivityMainBinding
import com.example.stubprojectkotlin.db.entites.User
import com.example.stubprojectkotlin.network.MainViewModel
import com.example.stubprojectkotlin.utils.LocationManager
import com.example.stubprojectkotlin.utils.Status.*
import dagger.hilt.android.AndroidEntryPoint
import com.dynamsoft.dbr.*;
import com.dynamsoft.dce.CameraEnhancer
import com.dynamsoft.dce.CameraEnhancerException


@AndroidEntryPoint
class MainActivity : BaseActivity() {

    var reader: BarcodeReader? = null
    var mCameraEnhancer: CameraEnhancer? = null


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

//            val workManagerService = BackgroundJobs(applicationContext)
//
//            workManagerService.createOneTimeWorkRequest()
//            workManagerService.getWorkInfoLiveData().observe(this , {
//                val status: String = it.state.name
//                Toast.makeText(this,status, Toast.LENGTH_SHORT).show()
//            })
        }

        mainViewModel.getMallsLive().observe(this, {
            when (it.status) {

                SUCCESS -> Log.d(tag, it.data.toString())
                ERROR -> it.message?.let { it1 -> Log.d(tag, it1) }
                LOADING -> TODO()
                INTERNET_CONNECTIVITY -> Toast.makeText(this , it.message , Toast.LENGTH_SHORT).show()
            }
        })



        try {
            // Create an instance of Dynamsoft Barcode Reader.
            reader = BarcodeReader()

            // Initialize license for Dynamsoft Barcode Reader.
            // The organization id 200001 here will grant you a public trial license good for 7 days. Note that network connection is required for this license to work.
            // If you want to use an offline license, please contact Dynamsoft Support: https://www.dynamsoft.com/company/contact/
            // You can also request a 30-day trial license in the customer portal: https://www.dynamsoft.com/customer/license/trialLicense?product=dbr&utm_source=installer&package=android
            val dbrParameters = DMDLSConnectionParameters()
            dbrParameters.organizationID = "200001"
            reader!!.initLicenseFromDLS(
                dbrParameters
            ) { isSuccessful, e ->
                runOnUiThread {
                    if (!isSuccessful) {
                        e.printStackTrace()
                        showErrorDialog(e.message!!)
                    }
                }
            }
        } catch (e: BarcodeReaderException) {
            e.printStackTrace()
        }

        // Create a listener to obtain the recognized barcode results.

        // Create a listener to obtain the recognized barcode results.
        val mTextResultCallback =
            TextResultCallback { i, textResults, userData ->
                // Obtain the recognized barcode results and display.
                runOnUiThread {

                    activityMainBinding.cameraView.alpha = 0.1f
                    showResult(textResults)
                }
            }

        // Create an instance of Dynamsoft Camera Enhancer for video streaming.

        // Create an instance of Dynamsoft Camera Enhancer for video streaming.
        mCameraEnhancer = CameraEnhancer(this@MainActivity)
        mCameraEnhancer!!.cameraView = activityMainBinding.cameraView

        // Bind the Camera Enhancer instance to the Barcode Reader instance.

        // Bind the Camera Enhancer instance to the Barcode Reader instance.
        reader!!.setCameraEnhancer(mCameraEnhancer!!)

        // Make this setting to get the result. The result will be an object that contains text result and other barcode information.

        // Make this setting to get the result. The result will be an object that contains text result and other barcode information.
        try {
            reader!!.setTextResultCallback(mTextResultCallback, null)
        } catch (e: BarcodeReaderException) {
            e.printStackTrace()
        }




//        GlobalScope.launch {
//            val result = mainViewModel.getUsersFromDB()
//
//            Log.d(tag , result[0].name)
//        }


    }

    override fun onResume() {
        // Start video barcode reading
        try {
            mCameraEnhancer!!.open()
        } catch (e: CameraEnhancerException) {
            e.printStackTrace()
        }
        reader!!.startScanning()
        super.onResume()
    }

    override fun onPause() {
        // Stop video barcode reading
        try {
            mCameraEnhancer!!.close()
        } catch (e: CameraEnhancerException) {
            e.printStackTrace()
        }
        reader!!.stopScanning()
        super.onPause()
    }




    private fun showResult(results: Array<TextResult>?) {
        if (results != null && results.size > 0) {
            var strRes = ""
            for (i in results.indices) strRes += """
     ${results[i].barcodeText}
     
     """.trimIndent()
           activityMainBinding.tvRes.text = strRes
            activityMainBinding.cameraView.alpha = 1f
        } else {
            activityMainBinding.tvRes.text = ""
        }
    }

    private fun showErrorDialog(message: String) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Verification Failed")
            .setPositiveButton("OK", null)
            .setMessage(message)
            .show()
    }

     private fun showUser(users : List<User>) {

         if (users.isNotEmpty())
           Log.d(tag , users.toString())
    }

}