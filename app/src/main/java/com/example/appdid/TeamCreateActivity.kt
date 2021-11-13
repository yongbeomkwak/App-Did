package com.example.appdid

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.appdid.databinding.ActivityAddTeamBinding
import com.example.appdid.databinding.AppBarTeamSettingBinding

class TeamCreateActivity :AppCompatActivity() {
    private lateinit var mBinding:ActivityAddTeamBinding
    private val binding get() = mBinding!!
    private lateinit var mAppBarBinding: AppBarTeamSettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityAddTeamBinding.inflate(layoutInflater)
        mAppBarBinding=binding.incAppBar
        setSupportActionBar(mAppBarBinding.appBar) //ActionBar 설정
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 설정
        supportActionBar!!.setTitle("팀 생") //actionBar title 설정
        setContentView(binding.root)



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

    override fun onBackPressed() {
        super.onBackPressed()

        finish()
    }

    override fun finish() {

        super.finish()
        overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_left_exit) //finish()호출 시 에니메이션 설정,툴바 뒤로가기를 위해

    }

}