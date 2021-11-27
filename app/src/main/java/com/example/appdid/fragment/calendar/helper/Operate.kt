package com.example.appdid.fragment.calendar.helper

import android.util.Log
import java.util.*

class Operate { // 날짜 연산 헬퍼 클래스

    companion object {
        fun isAfter(firCalendar: Calendar, secCalendar: Calendar): Boolean { // 앞의 달력이 뒤의 달력 보다 느린가
            if (firCalendar.get(Calendar.YEAR) != secCalendar.get(Calendar.YEAR)) {
                return firCalendar.get(Calendar.YEAR) > secCalendar.get(Calendar.YEAR)
            }
            if (firCalendar.get(Calendar.MONTH) != secCalendar.get(Calendar.MONTH)) {
                return firCalendar.get(Calendar.MONTH) > secCalendar.get(Calendar.MONTH)
            }
            return firCalendar.get(Calendar.DAY_OF_MONTH) > secCalendar.get(Calendar.DAY_OF_MONTH)
        }

        fun isSame(firCalendar: Calendar, secCalendar: Calendar) : Boolean { // 두 달력이 같은 날짜를 가르키는가
            return firCalendar.get(Calendar.YEAR) == secCalendar.get(Calendar.YEAR)
                    && firCalendar.get(Calendar.MONTH) == secCalendar.get(Calendar.MONTH)
                    && firCalendar.get(Calendar.DAY_OF_MONTH) == secCalendar.get(Calendar.DAY_OF_MONTH)
        }

        fun calendarToString(calendar: Calendar) : String {
            var year = calendar.get(Calendar.YEAR).toString()
            var month = (calendar.get(Calendar.MONTH)+1).toString()
            if (month.length <= 1) {
                month = "0" + month
            }
            var day = (calendar.get(Calendar.DAY_OF_MONTH)).toString()
            if (day.length <= 1) {
                day = "0" + day
            }
            return year + "-" + month + "-" + day
        }

        fun stringToCalendar(str: String) : Calendar {
            Log.e("WOW", str)
            var arr = str.split("T")[0].split("-")
            var year = arr[0].toInt()
            var month = arr[1].toInt() - 1
            var date = arr[2].toInt()

            var calendar = Calendar.getInstance()
            calendar.set(year, month, date)
            return calendar
        }
    }
}