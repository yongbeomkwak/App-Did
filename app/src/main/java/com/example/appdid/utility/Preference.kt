package com.example.appdid.utility

import android.content.Context
import android.content.SharedPreferences

class Preference(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("UserInfo",Context.MODE_PRIVATE)

    fun setString(key:String,str:String)
    {
        prefs.edit().putString(key,str).apply()
    }

    fun getString(key: String,defValue:String ="DefValue"):String
    {
        return prefs.getString(key,defValue).toString()
    }
}