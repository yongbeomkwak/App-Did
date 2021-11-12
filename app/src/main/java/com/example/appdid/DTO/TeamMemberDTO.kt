package com.example.appdid.DTO

class TeamMemberDTO(private val userName:String,private val userId:String) {

    


    fun getUserName():String
    {
        return this.userName
    }

    fun userId():String
    {
     return this.userId
    }


}