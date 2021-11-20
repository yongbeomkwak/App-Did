package com.example.appdid.DTO

import com.google.gson.annotations.SerializedName

data class PayloadDTO(
        @SerializedName("code")
        val code: Int,
        @SerializedName("payloads")
        val payloads: List<UserInfoDTO>
)
{
    override fun toString(): String {
        return "code"+ code +"/n" + "payloads:" +"/t" + payloads.toString()
    }
}
