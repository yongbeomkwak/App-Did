package com.example.appdid.dto

import com.google.gson.annotations.SerializedName

data class UserGroupDTO(
        @SerializedName("_id")
    val _id: String,
        @SerializedName("groupName")
    val groupName: String
)