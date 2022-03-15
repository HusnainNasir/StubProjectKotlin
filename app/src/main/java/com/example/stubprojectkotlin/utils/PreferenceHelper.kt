package com.example.stubprojectkotlin.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceHelper @Inject constructor(@ApplicationContext context: Context) {

    private val appPrefs: SharedPreferences =
        context.getSharedPreferences(Constants.DB_NAME, Context.MODE_PRIVATE)

    object PreferenceVariable {
        const val AUTH_TOKEN = "auth_token"
        const val USER_EMAIL = "user_email"
        const val USER_PASSWORD = "user_password"
    }

    private val editor: SharedPreferences.Editor = appPrefs.edit()


    fun setOnChange(listener: OnSharedPreferenceChangeListener?) {
        appPrefs.registerOnSharedPreferenceChangeListener(listener)
    }

    // Auth Token Setter/Getter
    var authToken: String
        get() = appPrefs.getString(PreferenceVariable.AUTH_TOKEN, "").toString()
        set(authToken) {
            editor.putString(PreferenceVariable.AUTH_TOKEN, authToken)
            editor.apply()
        }

    // USER EMAIL Setter/Getter
    var userEmail: String
        get() = appPrefs.getString(PreferenceVariable.USER_EMAIL, "").toString()
        set(email) {
            editor.putString(PreferenceVariable.USER_EMAIL, email)
            editor.apply()
        }

    // USER PASSWORD Setter/Getter
    var userPassword: String
        get() = appPrefs.getString(PreferenceVariable.USER_PASSWORD, "").toString()
        set(password) {
            editor.putString(PreferenceVariable.USER_PASSWORD, password)
            editor.apply()
        }

    init {
        editor.apply()
    }
}