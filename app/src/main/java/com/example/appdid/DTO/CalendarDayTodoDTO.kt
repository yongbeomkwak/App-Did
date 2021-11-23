package com.example.appdid.DTO

import com.example.appdid.R
import com.google.gson.annotations.SerializedName
import java.util.*

class CalendarDayTodoDTO (
    @SerializedName("_id")
    var _id: String = "", // 할일 id
    @SerializedName("")
    var beginCalendar: String = "yyyy-MM-dd", // 시작 달력
    @SerializedName("")
    var endCalendar: Calendar = Calendar.getInstance(), // 끝 달력
    @SerializedName("title")
    var title: String = "title", // Todo 이름
    @SerializedName("")
    var _group_id: String = "", // Todo 그룹 id
    @SerializedName("color")
    var color: Int = R.color.todo_1, // 배경색
        ) {
}