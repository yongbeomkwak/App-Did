package com.example.appdid.dto

import com.google.gson.annotations.SerializedName

data class GroupPayloadItem(
        @SerializedName("_id")
    val _id: String,
        @SerializedName("created_at")
    val created_at: String,
        @SerializedName("groupName")
    val groupName: String,
        @SerializedName("users")
    val users: List<TeamMemberDTO>
)