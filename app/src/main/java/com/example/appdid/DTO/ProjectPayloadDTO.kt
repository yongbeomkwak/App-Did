package com.example.appdid.DTO

import com.google.gson.annotations.SerializedName

data class ProjectPayloadDTO(
        @SerializedName("code")
    val code: Int,
        @SerializedName("payloads")
    val payloads: List<ProjectDTO>
)