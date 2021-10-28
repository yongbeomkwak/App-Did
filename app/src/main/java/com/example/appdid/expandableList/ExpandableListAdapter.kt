package com.example.appdid.expandableList

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.appdid.R

class ExpandableListAdapter(private val context:Context,private val parents:MutableList<String>,private  val childList:MutableList<MutableList<String>>) :BaseExpandableListAdapter() {
    override fun getGroupCount()=parents.size // 팀 개수

    override fun getChildrenCount(groupPosition: Int)=childList[groupPosition].size //해당 팀의 팀원 인원 수

    override fun getGroup(groupPosition: Int)=parents[groupPosition] //해당 팀 객체

    override fun getChild(groupPosition: Int, childPosition: Int)=childList[groupPosition][childPosition] //해당팀의 팀원 정보

    override fun getGroupId(groupPosition: Int)=groupPosition.toLong() //

    override fun getChildId(groupPosition: Int, childPosition: Int)=childPosition.toLong()

    override fun hasStableIds()=false

    override fun isChildSelectable(groupPosition: Int, childPosition: Int)=true

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parentview: ViewGroup?): View {
        //부모 계층 레이아웃 설정(팀 계층)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val parentView = inflater.inflate(R.layout.menu_parent,parentview,false)
        val textView:TextView=parentView.findViewById(R.id.tvTeamTitle)
        textView.text=parents[groupPosition]
        setIcon(groupPosition, parentView)
        setArrow(groupPosition, parentView, isExpanded)
        return parentView



    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parentView: ViewGroup?): View {
        //자식 계층 레이아웃(팀원 계층)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val childView = inflater.inflate(R.layout.menu_child,parentView,false)
        val textView:TextView =childView.findViewById(R.id.tvUser)
        textView.text=getChild(groupPosition,childPosition) //해당 팀에 해당하는 팀원의 이름
        return childView
    }



    private  fun setIcon(parentPosition:Int,parentView:View) //아이콘 설정
    {
        val imageView:ImageView=parentView.findViewById(R.id.ivTeam)
        imageView.setImageResource(R.drawable.ic_baseline_settings_24)
    }
    private fun setArrow(parentPosition: Int,parentView: View,isExpanded: Boolean)
    {
        val imageView:ImageView=parentView.findViewById(R.id.ivArrow)
        if(parentPosition!=0) // TODO: 첫번째는 주지 않음 수정 가능
        {
            if(isExpanded) imageView.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24) //확장 됬을 시 arrow 위로 변경
            else imageView.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24) // 닫혀 있을 시 arrow 아래로 변경
        }

    }



}