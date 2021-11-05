package com.example.appdid.fragment.calendar.day

import android.graphics.Color
import com.example.appdid.R

class CalendarInfo(
    var year: Int = 0, // 년도
    var month: Int = 0, // 달
    var dayOfMonth: Int = 0, // 해당 달의 날짜
    var dayOfWeek: Int = 0, // 요일 (일요일:0 ~ 토요일:6)
    var textColor: Int = R.color.calendar_gray, // 일 글자 색
    var groundTrans: Int = R.color.calendar_clear // 일 색
)