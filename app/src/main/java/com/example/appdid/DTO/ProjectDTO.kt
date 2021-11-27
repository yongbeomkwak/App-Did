package com.example.appdid.dto

import com.example.appdid.fragment.todo.project.TodoProjectInfo
import com.google.gson.annotations.SerializedName

data class ProjectDTO(
        @SerializedName("__v")
    val __v: Int,
        @SerializedName("_id")
    val _id: String,
        @SerializedName("created_at")
    val created_at: String,
        @SerializedName("groupId")
    val groupId: String,
        @SerializedName("projectName")
    val projectName: String,
        @SerializedName("todos")
    val todos: List<CalendarDayTodoDTO>
) {
    fun toInfo(): TodoProjectInfo {
        return TodoProjectInfo(_id, projectName, groupId, 0, todos, "#FFFFFF", created_at)
    }
}
