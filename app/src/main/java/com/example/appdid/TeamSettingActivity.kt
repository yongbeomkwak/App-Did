package com.example.appdid

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appdid.dto.GroupPayloadDTO
import com.example.appdid.dto.TeamMemberDTO
import com.example.appdid.RetrofitSet.RetrofitCreator
import com.example.appdid.RetrofitSet.RetrofitService
import com.example.appdid.adapter.TeamMemberAdapter
import com.example.appdid.adapter.TeamMemberHeightDecoration
import com.example.appdid.databinding.ActivityTeamSettingBinding
import com.example.appdid.databinding.AppBarTeamSettingBinding
import com.example.appdid.utility.MyApplication
import com.example.appdid.utility.ServerUri
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class TeamSettingActivity : AppCompatActivity() {
    private var mBinding:ActivityTeamSettingBinding?=null

    private lateinit var inviteCode:String
    private lateinit var mAppBarBinding:AppBarTeamSettingBinding
    private val binding get()=mBinding!!
    private var mintent:Intent? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_left_enter,R.anim.slide_right_exit) //새로운 엑티비티 왼쪽에서 들오고,현재엑티비티는 왼쪽으로 사라진다
        mintent=getIntent()
        inviteCode= mintent!!.getStringExtra("groupId").toString()
        mBinding= ActivityTeamSettingBinding.inflate(layoutInflater)
        mAppBarBinding=binding.incAppBar
        setSupportActionBar(mAppBarBinding.appBar) //ActionBar 설정
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 설정


        val retrofit:Retrofit=RetrofitCreator.defaultRetrofit(ServerUri.MyServer)
        val service:RetrofitService=retrofit.create(RetrofitService::class.java)
        val call:Call<GroupPayloadDTO> =service.getGroup(inviteCode, MyApplication.prefs.getString("token"))


        call.enqueue(object:Callback<GroupPayloadDTO>{
            override fun onResponse(call: Call<GroupPayloadDTO>, response: Response<GroupPayloadDTO>) {
                Log.e("RESSS",response.toString())
                Log.e("DTODTO",response.body().toString())
               if(response.isSuccessful)
               {
                   val payload:GroupPayloadDTO= response.body()!!
                   val arrayList=payload.payloads.users
                   initTeamMembers(arrayList)
                   supportActionBar!!.setTitle(response.body()!!.payloads.groupName) //actionBar title 설정

               }
            }

            override fun onFailure(call: Call<GroupPayloadDTO>, t: Throwable) {
                Log.e("RESSS",call.toString())
            }
        })




        //TODO 팀 멤버 정보 받아오기 , 이미지, 유저이름,아이디 등등




        binding.btnInviteCode.setOnClickListener{ //초대 코드 복사 리스너
            val clipBoard:ClipboardManager= getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip :ClipData=ClipData.newPlainText("InviteCode",inviteCode)
            clipBoard.setPrimaryClip(clip)
            Toast.makeText(this,"초대 코드가 성공적으로 복사 되었습니다.",Toast.LENGTH_LONG).show()
        }
        setContentView(binding.root)
    }


    fun initTeamMembers(arrayList:List<TeamMemberDTO>) // RecyclerView 초기화
    {
        binding.recTeamMembers.adapter=TeamMemberAdapter(this,arrayList) //어뎁터 설정
        binding.recTeamMembers.addItemDecoration(TeamMemberHeightDecoration(30)) //아이템 높이 간격 설정,데코레이션 추가
        binding.recTeamMembers.layoutManager=LinearLayoutManager(this) // 레이아웃 Linear
    }
    override fun onBackPressed() {
        super.onBackPressed()

        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { //뒤로가기 실질적인 동작

        when(item.itemId)
        {
            android.R.id.home->{ //id값은 android 기본 home값 내가만든 id값이 아님

                finish()
                false
            }
        }

        return super.onOptionsItemSelected(item)
    }
    override fun finish() {

        super.finish()
        overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_left_exit) //finish()호출 시 에니메이션 설정,툴바 뒤로가기를 위해

    }


}