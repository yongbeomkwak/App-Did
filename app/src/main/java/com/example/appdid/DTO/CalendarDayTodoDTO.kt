package com.example.appdid.DTO

import com.example.appdid.R
import com.google.gson.annotations.SerializedName
import java.util.*

class CalendarDayTodoDTO (
        @SerializedName("__v")
        val __v: Int,
        @SerializedName("_id")
        var _id: String = "", // 할일 id
        @SerializedName("author")
        var author: String = "", // 작성자 id
        @SerializedName("beginDate")
        var beginCalendar: String,
        @SerializedName("endDate")
        var endCalendar: String,
        @SerializedName("title")
        var title: String,
        @SerializedName("projectId")
        var _project_id: String,
        @SerializedName("color")
        var color: String,
        @SerializedName("check")
        var check: Boolean
) {
}