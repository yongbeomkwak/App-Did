package com.example.appdid.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.appdid.R
import com.example.appdid.databinding.AppBarTeamSettingBinding
import com.example.appdid.databinding.DialogAddTeamBinding
import com.example.appdid.databinding.DialogParticipateTeamBinding

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

            dialog.dismiss()
        }
    }







}