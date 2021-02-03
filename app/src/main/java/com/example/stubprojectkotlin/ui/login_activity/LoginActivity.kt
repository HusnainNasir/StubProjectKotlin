package com.example.stubprojectkotlin.ui.login_activity


import android.os.Bundle
import com.example.stubprojectkotlin.BaseActivity
import com.example.stubprojectkotlin.R

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override val layoutId: Int
        get() = R.layout.activity_login
    override val tag: String?
        get() = LoginActivity::class.simpleName

    override fun created(savedInstance: Bundle?) {

        init()
    }

    private fun init(){
        setToolbar(resources.getString(R.string.app_name))
    }
}