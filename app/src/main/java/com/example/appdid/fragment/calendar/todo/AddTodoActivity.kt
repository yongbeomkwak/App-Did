package com.example.appdid.fragment.calendar.todo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.appdid.databinding.ActivityAddTodoBinding

class AddTodoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTodoBinding
    private lateinit var buttonExit: Button // todo 추가 화면 나가기
    private lateinit var buttonAdd: Button // todo 추가
    private lateinit var buttonSetColor: Button // 색 선택 (dialog_todo_color을 띄워서 처리) 선택한 색에 따라 버튼 색도 달라야함
    private lateinit var edittextTitle: EditText // todo 제목
    private lateinit var buttonBeginTodo: Button // 시작 일 선택
    private lateinit var buttonEndTodo: Button // 끝 일 선택

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTodoBinding.inflate(layoutInflater)

        edittextTitle = binding.editTodoTitle
        buttonExit = binding.buttonExitAddTodo
        buttonSetColor = binding.buttonTodoColor
        buttonAdd = binding.buttonAddTodo

        buttonBeginTodo = binding.buttonBeginTodo
        buttonEndTodo = binding.buttonEndTodo
    }
}