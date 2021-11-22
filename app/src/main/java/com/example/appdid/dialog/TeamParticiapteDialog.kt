package com.example.appdid.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appdid.DTO.CodeMessageDTO
import com.example.appdid.MainActivity
import com.example.appdid.R
import com.example.appdid.RetrofitSet.RetrofitCreator
import com.example.appdid.RetrofitSet.RetrofitService
import com.example.appdid.databinding.AppBarTeamSettingBinding
import com.example.appdid.databinding.DialogAddTeamBinding
import com.example.appdid.databinding.DialogParticipateTeamBinding
import com.example.appdid.utility.MyApplication
import com.example.appdid.utility.ServerUri
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class TeamParticiapteDialog(private val context: Context) {
    private val dialog=Dialog(context)
    private lateinit var mBinding:DialogParticipateTeamBinding
    private val binding get() = mBinding!!


    fun setDialog()
    {
        mBinding= DialogParticipateTeamBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        binding.btnInvite.setOnClickListener {
            val retrofit: Retrofit = RetrofitCreator.defaultRetrofit(ServerUri.MyServer)
            val service: RetrofitService =retrofit.create(RetrofitService::class.java)
            val call: Call<CodeMessageDTO> =service.joinGroup(
                    binding.etInviteCode.text.toString(),
                    MyApplication.prefs.getString("id",""
            ), MyApplication.prefs.getString("token"))

            call.enqueue(object : Callback<CodeMessageDTO> {
                override fun onResponse(call: Call<CodeMessageDTO>, response: Response<CodeMessageDTO>) {
                    Log.e("RESS",response.toString())
                    if(response.isSuccessful)
                    {
                        val payload: CodeMessageDTO =response.body()!!
                        Toast.makeText(context,payload.message, Toast.LENGTH_LONG).show()
                        println(payload.toString())
                    }
                }

                override fun onFailure(call: Call<CodeMessageDTO>, t: Throwable) {
                    Log.e("Response","Error")
                }
            })


            dialog.setOnDismissListener(object: DialogInterface.OnDismissListener {
                override fun onDismiss(dialog: DialogInterface?) {
                    (context as MainActivity).reFreshTeamList()
                }

            })
            dialog.dismiss()
        }
    }







}