package com.example.appdid.dto

import com.google.gson.annotations.SerializedName

data class GroupPayloadDTO(
        @SerializedName("code")
        val code: Int,
        @SerializedName("payloads")
        val payloads: GroupPayloadItem
)