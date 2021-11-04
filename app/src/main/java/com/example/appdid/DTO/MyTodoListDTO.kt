package com.example.appdid.DTO

data class MyTodoListDTO(
    val code: Int,
    val payloads: List<MyTodoDTO>


) {
    override fun toString(): String {
        return "code"+ code +"/n" + "payloads:" +"/t" + payloads.toString()
    }
}