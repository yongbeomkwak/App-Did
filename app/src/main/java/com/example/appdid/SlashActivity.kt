package com.example.appdid

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.appdid.databinding.ActivitySplashBinding

class SlashActivity:AppCompatActivity() {
    private var mBinding:ActivitySplashBinding?=null
    private val bind get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(bind.root)
        val imageViewAnim:Animation=AnimationUtils.loadAnimation(this,R.anim.anim_splash_imageview)
        val textViewAnim:Animation=AnimationUtils.loadAnimation(this,R.anim.anim_splash_textview)
        bind.tvTitle.startAnimation(textViewAnim)
        bind.ivLogo.startAnimation(imageViewAnim)

        imageViewAnim.setAnimationListener(object :Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                //애니메이션 시작
            }

            override fun onAnimationEnd(animation: Animation?) {
                //에니메이션 끝날때
                val intent:Intent=Intent(applicationContext,LoginActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_up_enter,R.anim.slide_up_exit)
                finish()
            }

            override fun onAnimationRepeat(animation: Animation?) {
                //반복
            }
        })





    }
}