package com.example.appdid.dto

import com.example.appdid.R
import com.example.appdid.fragment.calendar.helper.Operate
import com.example.appdid.fragment.calendar.todo.day.CalendarDayTodoInfo
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
        fun toCalendarTodo() : CalendarDayTodoInfo {
                val calendarTodo: CalendarDayTodoInfo = CalendarDayTodoInfo(
                        _id,
                        author,
                        Operate.stringToCalendar(beginCalendar),
                        Operate.stringToCalendar(endCalendar),
                        title,
                        0,
                        _project_id,
                        color,
                        check,
                        -1
                )
                return calendarTodo
        }
}