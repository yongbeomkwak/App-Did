package com.example.appdid.DTO

import com.google.gson.annotations.SerializedName

data class CodeMessageDTO(
        @SerializedName("code")
        val code: Int,
        @SerializedName("message")
        val message: String)
{
    override fun toString(): String {
        return "${code}\n ${message}"
    }
}
