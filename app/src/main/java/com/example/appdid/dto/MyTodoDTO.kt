package com.example.appdid.dto

import com.google.gson.annotations.SerializedName

data class MyTodoDTO(
        @SerializedName("__v")
    val __v: Int,
        @SerializedName("_id")
    val _id: String,
        @SerializedName("author")
    val author: String,
        @SerializedName("check")
    val check: Boolean,
        @SerializedName("created_at")
    val created_at: String,
        @SerializedName("deadline")
    val deadline: String,
        @SerializedName("title")
    val title: String
)