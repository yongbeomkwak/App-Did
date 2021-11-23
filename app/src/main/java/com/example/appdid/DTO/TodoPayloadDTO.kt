package com.example.appdid.DTO

import com.google.gson.annotations.SerializedName

data class TodoPayloadDTO (
    @SerializedName("code")
    val code: Int,

    @SerializedName("payload")
    val payloads: List<CalendarDayTodoDTO>
        ) {

    override fun toString(): String {
        return "code" + code + "\n" +
                "payloads:\t" + payloads.toString()
    }
}