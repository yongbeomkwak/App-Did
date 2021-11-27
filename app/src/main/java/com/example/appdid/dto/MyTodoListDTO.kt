package com.example.appdid.dto

import com.google.gson.annotations.SerializedName

data class MyTodoListDTO(
        @SerializedName("code")
    val code: Int,
        @SerializedName("payloads")
    val payloads: List<MyTodoDTO>


) {
    override fun toString(): String {
        return "code"+ code +"/n" + "payloads:" +"/t" + payloads.toString()
    }
}