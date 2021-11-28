package com.example.appdid.dialog

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appdid.dto.CodeMessageDTO
import com.example.appdid.dto.UserGroupDTO
import com.example.appdid.MainActivity
import com.example.appdid.RetrofitSet.RetrofitCreator
import com.example.appdid.RetrofitSet.RetrofitService
import com.example.appdid.TeamSettingActivity
import com.example.appdid.databinding.DialogBottomSheetBinding
import com.example.appdid.utility.MyApplication
import com.example.appdid.utility.ServerUri
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class BottomSheetDialog(private val team:UserGroupDTO) : BottomSheetDialogFragment() {
    private var mBinding:DialogBottomSheetBinding?=null
    private val binding get()=mBinding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mBinding= DialogBottomSheetBinding.inflate(inflater,container,false)
        binding.tvTeamTitle.setText(team.groupName)
        binding.llTeamOut.setOnClickListener(onClick)
        binding.llTeamSetting.setOnClickListener(onClick)
        return(binding.root)


    }

    val onClick=object :View.OnClickListener{
        override fun onClick(v: View?) {
            when(v!!.id)
            {
                binding.llTeamOut.id -> {
                    val retrofit: Retrofit = RetrofitCreator.defaultRetrofit(ServerUri.MyServer)
                    val service: RetrofitService = retrofit.create(RetrofitService::class.java)
                    val call: Call<CodeMessageDTO> = service.removeGroup(
                          team._id,MyApplication.prefs.getString("id",""), MyApplication.prefs.getString("token")
                    )
                    call.enqueue(object :Callback<CodeMessageDTO>{
                        override fun onResponse(call: Call<CodeMessageDTO>, response: Response<CodeMessageDTO>) {
                            Log.e("RemoveTask",response.toString())
                        }

                        override fun onFailure(call: Call<CodeMessageDTO>, t: Throwable) {
                            Log.e("RemoveTask","Wrong")
                        }
                    })


                    dismiss()
                }
                binding.llTeamSetting.id->{
                    val intent:Intent=Intent(context,TeamSettingActivity::class.java)
                    intent.putExtra("groupId",team._id)
                    dismiss()
                    startActivity(intent)

                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (context as MainActivity).reFreshTeamList(true)
        (context as MainActivity).closeDrawer()


    }
}