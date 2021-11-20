package com.example.appdid.utility

import android.app.Application
import com.example.appdid.DTO.UserGroupDTO

class MyApplication : Application() {

    companion object
    {
        lateinit var prefs: Preference
        var userEmail:String?=null
        var userName:String?=null
        var userId:String?=null

        var TeamInfo:List<UserGroupDTO> = mutableListOf()

    }
    override fun onCreate() {
        super.onCreate()
        prefs= Preference(applicationContext)
        userEmail=prefs.getString("email","noEmail")
        userName= prefs.getString("name","Noname")
        userId=prefs.getString("id","noId")
    }
}