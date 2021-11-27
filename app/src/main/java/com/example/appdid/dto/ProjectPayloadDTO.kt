package com.example.appdid.dto

import com.google.gson.annotations.SerializedName

data class ProjectPayloadDTO(
        @SerializedName("code")
    val code: Int,
        @SerializedName("payloads")
    val payloads: List<ProjectDTO>
)