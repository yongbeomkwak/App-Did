package com.example.appdid.fragment.todo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.appdid.dto.ProjectsAndTodosPayloadDTO
import com.example.appdid.RetrofitSet.RetrofitCreator
import com.example.appdid.RetrofitSet.RetrofitService
import com.example.appdid.databinding.FragmentTodoBinding
import com.example.appdid.fragment.calendar.CalendarFragment
import com.example.appdid.fragment.todo.project.TodoProjectAdapter
import com.example.appdid.fragment.todo.project.TodoProjectInfo
import com.example.appdid.utility.MyApplication
import com.example.appdid.utility.ServerUri
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class TodoFragment: Fragment() {    // Todo리스트 Fragment
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TodoProjectAdapter
    private lateinit var pageChangeCallback: CalendarFragment.PageChangeCallback

    lateinit var root: View
    lateinit var recyclerView: RecyclerView
    lateinit var buttonAdd: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        root = binding.root

        recyclerView = binding.todoProjectRecycler
        adapter = TodoProjectAdapter()
        recyclerView.adapter = adapter

        loadTodoProject(adapter)

        buttonAdd = binding.buttonAddTodoProject
        buttonAdd.setOnClickListener{
        }

        return root
    }

    fun loadTodoProject(adapter: TodoProjectAdapter) { // 서버에서 todo 정보 가져옴
        val groupId = MyApplication.prefs.getString("groupId")
        val retrofit: Retrofit = RetrofitCreator.defaultRetrofit(ServerUri.MyServer)
        val service: RetrofitService = retrofit.create(RetrofitService::class.java)

        Log.e("WOW", "GROUPID" + groupId)
        val call: Call<ProjectsAndTodosPayloadDTO> =service.getProjectsAndTodos(groupId,MyApplication.prefs.getString("token"))
        call.enqueue(object: Callback<ProjectsAndTodosPayloadDTO>
        {
            override fun onResponse(call: Call<ProjectsAndTodosPayloadDTO>, response: Response<ProjectsAndTodosPayloadDTO>) {
                if(response.isSuccessful)
                {
                    var projectList: MutableList<TodoProjectInfo> = mutableListOf()
                    for (payload in response.body()!!.payloads) {
                        projectList.add(payload.toInfo())
                        if (projectList.last().todos.size > 0) {
                            projectList.last().color = projectList.last().todos[0].color
                        } else {
                            projectList.last().color = "#2062AF"
                        }
                        for (todo in projectList.last().todos) {
                            if (todo.check) {
                                projectList.last().progress++
                            }
                        }
                        if (projectList.last().todos.size > 0) {
                            projectList.last().progress =
                                projectList.last().progress / projectList.last().todos.size * 100
                        }
                    }

                    projectList.sortByDescending { it.created_at }
                    initTodoProject(adapter, projectList)
                }
            }

            override fun onFailure(call: Call<ProjectsAndTodosPayloadDTO>, t: Throwable) {
                Log.e("TODO","ERROR")
            }
        })
    }

    fun initTodoProject(adapter: TodoProjectAdapter, projectList: MutableList<TodoProjectInfo>) {
        adapter.submitList(projectList)
    }
}