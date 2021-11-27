package com.example.appdid.fragment.todo

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.appdid.dto.CodeMessageDTO
import com.example.appdid.dto.ProjectDTO
import com.example.appdid.dto.ProjectPayloadDTO
import com.example.appdid.R
import com.example.appdid.RetrofitSet.RetrofitCreator
import com.example.appdid.RetrofitSet.RetrofitService
import com.example.appdid.databinding.ActivityAddTodoBinding
import com.example.appdid.utility.MyApplication
import com.example.appdid.utility.ServerUri
import petrov.kristiyan.colorpicker.ColorPicker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*

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
    private lateinit var beginDate:String
    private lateinit var endDate:String
    private lateinit var retrofit:Retrofit
    private lateinit var  service:RetrofitService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        teamId=intent.getStringExtra("groupId").toString()
        toDoColor="#2062AF" //기본 색

        retrofit=RetrofitCreator.defaultRetrofit(ServerUri.MyServer)
        service=retrofit.create(RetrofitService::class.java)

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
                .setDefaultColorButton(Color.parseColor(toDoColor))
                .setColumns(5)
                .disableDefaultButtons(true)
                .setRoundColorButton(true) //동그란 색깔 화면
                .show();

        }

        buttonAdd = binding.buttonAddTodo
        buttonAdd.setOnClickListener {
            //보낼 데이터 , beginData,endDate,제목,색깔,프로젝트 \
            val call:Call<CodeMessageDTO> =service.postProject(mapOf(
                    "userId" to MyApplication.prefs.getString("id"),
                    "beginDate" to beginDate,
                    "endDate" to endDate,
                    "color" to toDoColor,
                    "title" to edittextTitle.text.toString(),
                    "projectId" to projectCode,
                    "groupId" to teamId
            ),MyApplication.prefs.getString("token"))

            call.enqueue(object :Callback<CodeMessageDTO>{
                override fun onResponse(call: Call<CodeMessageDTO>, response: Response<CodeMessageDTO>) {

                    if(response.isSuccessful)
                    {
                        Toast.makeText(applicationContext,response.body()!!.message,Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onFailure(call: Call<CodeMessageDTO>, t: Throwable) {
                    //
                }
            })


            //보낸 후

        }

        buttonBeginTodo = binding.buttonBeginTodo
        buttonBeginTodo.setOnClickListener {
            val cal= Calendar.getInstance()
            val dateSetListener=DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                if(month+1<10)
                {
                    if(dayOfMonth<10)
                    {
                        beginDate="${year}-0${month+1}-0${dayOfMonth}"
                    }
                    else
                    {
                        beginDate="${year}-0${month+1}-${dayOfMonth}"
                    }

                }
                else
                {
                    if(dayOfMonth<10)
                    {
                        beginDate="${year}-${month+1}-0${dayOfMonth}"
                    }
                    else
                    {
                        beginDate="${year}-${month+1}-${dayOfMonth}"
                    }

                }

                buttonBeginTodo.text=beginDate
            }
            DatePickerDialog(this,dateSetListener,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        buttonEndTodo = binding.buttonEndTodo
        buttonEndTodo.setOnClickListener {
            val cal= Calendar.getInstance()
            val dateSetListener=DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                if(month+1<10)
                {

                    if(dayOfMonth<10)
                    {
                        endDate="${year}-0${month+1}-0${dayOfMonth}"
                    }
                    else
                    {
                        endDate="${year}-0${month+1}-${dayOfMonth}"
                    }
                }
                else
                {
                    if(dayOfMonth<10)
                    {
                        endDate="${year}-${month+1}-0${dayOfMonth}"
                    }
                    else
                    {
                        endDate="${year}-${month+1}-${dayOfMonth}"
                    }

                }


                buttonEndTodo.text=endDate
            }
            DatePickerDialog(this,dateSetListener,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        buttonExit.setOnClickListener {
            //뒤로가기
            finish()
        }
        getProjects()


    }

    fun getProjects()
    {

        projects= mutableListOf("해당 프로젝트를 선택해주세요",)
        projectsIds= mutableListOf("0",)
        val call:Call<ProjectPayloadDTO> =service.getProjects(teamId,MyApplication.prefs.getString("token"))
        call.enqueue(object:Callback<ProjectPayloadDTO>{
            override fun onResponse(call: Call<ProjectPayloadDTO>, response: Response<ProjectPayloadDTO>) {
                Log.e("Teamid: ",response.toString())
                Log.e("RESULT",response.body().toString())
                if(response.isSuccessful)
                {
                    val palyoad:List<ProjectDTO> =response.body()!!.payloads

                    for (project in palyoad)
                    {
                        Log.e("Projects",project.projectName)
                        projects.add(project.projectName)
                        projectsIds.add(project._id)
                    }

                }
            }

            override fun onFailure(call: Call<ProjectPayloadDTO>, t: Throwable) {
                Log.e("ADD_RES",t.message.toString())
            }
        })
        //TODO groupId를 보내서 해당 그룹의 프로젝트들을 불러오는 것


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
                    projectCode=projectsIds[position]
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }

    }
}