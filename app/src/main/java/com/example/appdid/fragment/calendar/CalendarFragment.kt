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
            val retrofit: Retrofit = RetrofitCreator.defaultRetrofit(ServerUri.testUri) //빌더
            val service:RetrofitService=retrofit.create(RetrofitService::class.java)//인터페이스
            val call:Call<TestDto> =service.getPosts("1") //해당 인터페이스를 통한 RUST 적
            call.enqueue(object :Callback<TestDto>{용 //콜백함수
                override fun onResponse(call: Call<TestDto>, response: Response<TestDto>) {

                    Log.d("result",response.toString())
                    if(response.isSuccessful())
                    {
                        val result:TestDto = response.body()!!
                        Log.d("result",result.toString())
                    }
                    else
                    {
                        Log.d("result","실패")
                    }
                }

                override fun onFailure(call: Call<TestDto>, t: Throwable) {
                    Log.d("result","onFailure" + t.message)
                }
            })
        }
    }

}