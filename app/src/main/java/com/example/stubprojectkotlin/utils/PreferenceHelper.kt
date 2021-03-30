package com.example.stubprojectkotlin.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceHelper @Inject constructor(@ApplicationContext context: Context) {

    private val appPrefs: SharedPreferences = context.getSharedPreferences(Constants.DB_NAME, Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor = appPrefs.edit()
    private val AUTH_TOKEN = "auth_token"
    private val USER_EMAIL = "user_email"
    private val USER_PASSWORD = "user_password"

    fun setOnChange(listener: OnSharedPreferenceChangeListener?) {
        appPrefs.registerOnSharedPreferenceChangeListener(listener)
    }

    // Auth Token Setter/Getter
    var authToken: String
        get() = appPrefs.getString(AUTH_TOKEN, "").toString()
        set(authToken) {
            editor.putString(AUTH_TOKEN, authToken)
            editor.apply()
        }

    // USER EMAIL Setter/Getter
    var userEmail: String
        get() = appPrefs.getString(USER_EMAIL, "").toString()
        set(email) {
            editor.putString(USER_EMAIL, email)
            editor.apply()
        }

    // USER PASSWORD Setter/Getter
    var userPassword: String
        get() = appPrefs.getString(USER_PASSWORD, "").toString()
        set(password) {
            editor.putString(USER_PASSWORD, password)
            editor.apply()
        }

    init {
        editor.apply()
    }
}