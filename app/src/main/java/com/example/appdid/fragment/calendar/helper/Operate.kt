package com.example.appdid.fragment.calendar.helper

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
    }
}