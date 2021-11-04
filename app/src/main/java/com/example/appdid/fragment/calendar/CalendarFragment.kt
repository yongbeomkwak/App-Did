package com.example.appdid.fragment.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.appdid.DTO.MyTodoDTO
import com.example.appdid.DTO.MyTodoListDTO
import com.example.appdid.DTO.TestDto
import com.example.appdid.RetrofitSet.RetrofitCreator
import com.example.appdid.RetrofitSet.RetrofitService
import com.example.appdid.databinding.FragmentCalendarBinding
import com.example.appdid.utility.ServerUri
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class CalendarFragment : Fragment() {   // 달력 Fragment
    private var _binding: FragmentCalendarBinding? = null

    private lateinit var button1: Button

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        button1 = binding.button1
        button1.setOnClickListener(button1ClickListener)
        return root
    }

    val button1ClickListener : View.OnClickListener = object : View.OnClickListener{
        override fun onClick(v: View?) {
            val retrofit: Retrofit = RetrofitCreator.defaultRetrofit(ServerUri.MyServer) //빌더
            val service:RetrofitService=retrofit.create(RetrofitService::class.java)//인터페이스
            val call:Call<MyTodoListDTO> =service.getMyTodoList("123412341234") //해당 인터페이스를 통한 RUST 접근
            call.enqueue(object:Callback<MyTodoListDTO>{
                override fun onResponse(call: Call<MyTodoListDTO>, response: Response<MyTodoListDTO>) {
                    if(response.isSuccessful)
                    {
                        val myTodoList: MyTodoListDTO =response.body()!!

                        Log.e("RES",myTodoList.payloads[0].title)



                    }
                    else
                    {
                        Log.e("RES",response.toString())
                    }
                }

                override fun onFailure(call: Call<MyTodoListDTO>, t: Throwable) {
                    Log.e("RES",t.message.toString())
                }
            })
        }
    }

}
