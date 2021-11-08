package com.example.appdid.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.appdid.databinding.DialogBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialog(private val teamName:String) : BottomSheetDialogFragment() {
    private var mBinding:DialogBottomSheetBinding?=null
    private val binding get()=mBinding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding= DialogBottomSheetBinding.inflate(inflater,container,false)
        binding.tvTeamTitle.setText(teamName)
        binding.llTeamOut.setOnClickListener(onClick)
        binding.llTeamSetting.setOnClickListener(onClick)
        return(binding.root)

    }

    val onClick=object :View.OnClickListener{
        override fun onClick(v: View?) {
            when(v!!.id)
            {
                binding.llTeamOut.id ->{
                    Toast.makeText(context,"TeamOut",Toast.LENGTH_SHORT).show()
                }
                binding.llTeamSetting.id->{
                    Toast.makeText(context,"TeamSetting",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}