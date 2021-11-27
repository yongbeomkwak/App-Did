package com.example.appdid.fragment.todo.project

import com.example.appdid.dto.CalendarDayTodoDTO

class TodoProjectInfo (
    val _id: String = "", // 프로젝트 id
    val totle: String = "", // 제목
    val groupId: String = "", // 그룹 이름
    var progress: Int = 0, // 공정률
    val todos: List<CalendarDayTodoDTO>, // 프로젝트 내 todo list
    var color: String = "", // 색깔
    var created_at: String = "",
        ) {
}