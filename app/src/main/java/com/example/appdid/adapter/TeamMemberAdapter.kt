package com.example.appdid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appdid.DTO.TeamMemberDTO
import com.example.appdid.databinding.SubLayoutTeamMemberBinding

// 뷰바인딩을 이용한 Adapter설정
class TeamMemberAdapter(private val context:Context,private val members:ArrayList<TeamMemberDTO>) : RecyclerView.Adapter<TeamMemberAdapter.TeamMemberHolder>() {
    private var mBinding:SubLayoutTeamMemberBinding? =null
    private val binding get() = mBinding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamMemberHolder {
        //context로 부터 LayoutInflater 가져와 바인딩 후
        //홀더 매개변수에 binding 너허줌
        mBinding= SubLayoutTeamMemberBinding.inflate(LayoutInflater.from(context),parent,false)
        return(TeamMemberHolder(binding))
    }

    override fun onBindViewHolder(holder: TeamMemberHolder, position: Int) {

        //with(인스턴스) 키워드를 사용하용하여 holder.xx 가아닌 그냥 xx로 사용 여기서는 binding이 holder.binding이 아닌 binding으로 사용
        with(holder)
        {
            with(members[position])
            {
                binding.tvTeamMemberName.text=getUserName()


            }
        }

    }



    override fun getItemCount(): Int {
        return members.size
    }

    inner class TeamMemberHolder(binding: SubLayoutTeamMemberBinding):RecyclerView.ViewHolder(binding.root)
    //ViewHolder 매개변수에는 binding.root ->view

}


