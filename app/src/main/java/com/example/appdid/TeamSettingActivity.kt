package com.example.appdid

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.example.appdid.databinding.ActivityTeamSettingBinding
import com.example.appdid.databinding.AppBarTeamSettingBinding

class TeamSettingActivity : AppCompatActivity() {
    private var mBinding:ActivityTeamSettingBinding?=null


    private lateinit var mAppBarBinding:AppBarTeamSettingBinding
    private val binding get()=mBinding!!
    private var mintent:Intent? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_left_enter,R.anim.slide_right_exit) //새로운 엑티비티 왼쪽에서 들오고,현재엑티비티는 왼쪽으로 사라진다
        mintent=getIntent()
        mBinding= ActivityTeamSettingBinding.inflate(layoutInflater)
        mAppBarBinding=binding.incAppBar
        setSupportActionBar(mAppBarBinding.appBar) //ActionBar 설정
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 설정
        supportActionBar!!.setTitle(mintent!!.getStringExtra("TeamName")) //actionBar title 설정




        setContentView(binding.root)

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