package com.example.appdid.fragment.todo

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.appdid.R
import com.example.appdid.databinding.ActivityAddTodoBinding
import petrov.kristiyan.colorpicker.ColorPicker

class AddTodoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTodoBinding
    private lateinit var buttonExit: Button // todo 추가 화면 나가기
    private lateinit var buttonAdd: Button // todo 추가
    private lateinit var buttonSetColor: Button // 색 선택 (dialog_todo_color을 띄워서 처리) 선택한 색에 따라 버튼 색도 달라야함
    private lateinit var edittextTitle: EditText // todo 제목
    private lateinit var buttonBeginTodo: Button // 시작 일 선택
    private lateinit var buttonEndTodo: Button // 끝 일 선택
    private lateinit var teamId:String // 해당 팀 아이디
    private lateinit var projectCode:String //해당 프로젝트 아디
    private lateinit var projects:MutableList<String> //프로젝트 명 리스트
    private lateinit var projectsIds:MutableList<String> //프로젝트 아이디
    private lateinit var toDoColor:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teamId=intent.getStringExtra("groupId").toString()
        toDoColor=String.format("#%06X", (0xFFFFFF and  R.color.primary_color)) //기본 색


        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        edittextTitle = binding.editTodoTitle
        buttonExit = binding.buttonExitAddTodo

        buttonSetColor = binding.buttonTodoColor
        buttonSetColor.setOnClickListener {
            val colorPicker:ColorPicker= ColorPicker(this)
            colorPicker.setOnFastChooseColorListener(object :
                ColorPicker.OnFastChooseColorListener {
                override fun setOnFastChooseColorListener(position: Int, color: Int) {
                    val hexColor = String.format("#%06X", (0xFFFFFF and  color))
                    buttonSetColor.setBackgroundColor(Color.parseColor(hexColor))
                    toDoColor=hexColor //
                    Toast.makeText(applicationContext, hexColor, Toast.LENGTH_SHORT).show()
                }

                override fun onCancel() {
                    // TODO("Not yet implemented")
                }
            })
                .setDefaultColorButton(R.color.primary_color)
                .setColumns(5)
                .disableDefaultButtons(true)
                .setRoundColorButton(true) //동그란 색깔 화면
                .show();

        }

        buttonAdd = binding.buttonAddTodo

        buttonBeginTodo = binding.buttonBeginTodo
        buttonEndTodo = binding.buttonEndTodo
        buttonExit.setOnClickListener {
            //뒤로가기
            finish()
        }

        getProjects()

    }

    fun getProjects()
    {
        //TODO groupId를 보내서 해당 그룹의 프로젝트들을 불러오는 것
        projects= mutableListOf("해당 프로젝트를 선택해주세요", "1234", "5678")
        projectsIds= mutableListOf("0", "12", "12")

        val adapter:ArrayAdapter<String> = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            projects
        )
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        binding.spProject.adapter=adapter

        binding.spProject.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position!=0)
                {
                    Toast.makeText(applicationContext, projects[position], Toast.LENGTH_SHORT).show()
                    projectCode=projectsIds[position]
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }

    }
}