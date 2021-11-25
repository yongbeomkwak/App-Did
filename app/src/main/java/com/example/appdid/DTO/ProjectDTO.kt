package com.example.appdid.DTO

import com.google.gson.annotations.SerializedName

data class ProjectDTO(
        @SerializedName("__v")
    val __v: Int,
        @SerializedName("_id")
    val _id: String,
        @SerializedName("create_ad")
    val created_at: String,
        @SerializedName("groupId")
    val groupId: String,
        @SerializedName("projectName")
    val projectName: String
)