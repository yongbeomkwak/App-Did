package com.example.appdid.DTO

import com.google.gson.annotations.SerializedName

data class TeamMemberDTO(
        @SerializedName("name")
        private val userName:String,
        @SerializedName("_id")
        private val userId:String
        ){

        fun getName():String
        {
                return userName
        }
        fun getId():String
        {
                return userId
        }
}


