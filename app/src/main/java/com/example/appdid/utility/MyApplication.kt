package com.example.appdid.utility

import android.app.Application

class MyApplication : Application() {

    companion object
    {
        lateinit var prefs: Preference
        var userEmail:String?=null
        var userName:String?=null

    }
    override fun onCreate() {
        super.onCreate()
        prefs= Preference(applicationContext)
        userEmail=prefs.getString("email","noEmail")
        userName= prefs.getString("name","Noname")
    }
}