package com.example.myfamily

import android.content.Context
import android.content.SharedPreferences

object  SharedPref {

    private const val NAME = "MyFamilySharedPreference"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    fun putBoolean(key: String, value: Boolean){
        preferences.edit().putBoolean(key, value).commit()
    }

    fun getBooleans(key: String): Boolean{
        return preferences.getBoolean(key, false)
    }

    fun putString(key: String, value: String) {
        preferences.edit().putString(key, value).commit()
    }

    fun getString(key: String): String? {
        return preferences.getString(key, null)
    }
}