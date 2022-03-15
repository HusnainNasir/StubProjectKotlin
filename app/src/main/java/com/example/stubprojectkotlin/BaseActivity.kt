package com.example.stubprojectkotlin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.stubprojectkotlin.utils.PreferenceHelper
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {
    // Getters/Setters
    @Inject
    lateinit var preferenceHelper: PreferenceHelper
    @Inject
    lateinit var gSON: Gson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        created(savedInstanceState)
    }


//    private val locationSettingDialog =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                result.data
//            }
//        }


    // setToolbar
    fun setToolbar(title: String?) {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // Activity Navigation
    fun activityNavigation(
        context: Context?, activityClass: Class<*>?, isFinish: Boolean,
        dataName: List<String>, dataValues: List<Any>
    ) {

        val activityIntent = Intent(context, activityClass)
        val incrementData = 0
        for (intentValue in dataName) {
            activityIntent.putExtra(intentValue, gSON.toJson(dataValues[incrementData]))
        }
        startActivity(activityIntent)
        if (isFinish) finish()
    }

    // Abstract Method
    protected abstract val layoutId: View
    protected abstract val tag: String?
    protected abstract fun created(savedInstance: Bundle?)
}