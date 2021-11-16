package com.example.appdid.fragment.calendar.day

import android.graphics.Color
import com.example.appdid.R
import java.util.*

class CalendarInfo(
    var dayCalendar: Calendar = Calendar.getInstance(), // 해당 날짜 달력
    var textColor: Int = R.color.calendar_gray, // 일 글자 색
    var groundTrans: Int = R.color.calendar_clear, // 일 색
    var height: Int = 60 // 날짜의 높이
)