package com.example.appdid.expandableList

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.media.Image
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.example.appdid.R
import com.example.appdid.databinding.AppBarMainBinding
import com.example.appdid.databinding.MenuChildBinding
import com.example.appdid.databinding.MenuParentBinding
import com.example.appdid.dialog.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ExpandableListAdapter(private val context: Context,private  val fragmentManager: FragmentManager, private val parents: MutableList<String>, private val childList: MutableList<MutableList<String>>) : BaseExpandableListAdapter() {
    private lateinit var parentBinding:MenuParentBinding
    private lateinit var childBinding: MenuChildBinding

    override fun getGroupCount() = parents.size // 팀 개수

    override fun getChildrenCount(groupPosition: Int) = childList[groupPosition].size //해당 팀의 팀원 인원 수

    override fun getGroup(groupPosition: Int) = parents[groupPosition] //해당 팀 객체

    override fun getChild(groupPosition: Int, childPosition: Int) = childList[groupPosition][childPosition] //해당팀의 팀원 정보

    override fun getGroupId(groupPosition: Int) = groupPosition.toLong() //

    override fun getChildId(groupPosition: Int, childPosition: Int) = childPosition.toLong()

    override fun hasStableIds() = false

    override fun isChildSelectable(groupPosition: Int, childPosition: Int) = true

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parentview: ViewGroup?): View {
        //부모 계층 레이아웃 설정(팀 계층)

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        parentBinding= MenuParentBinding.inflate(inflater,parentview,false)

        //val parentView = inflater.inflate(R.layout.menu_parent, parentview, false)
        //val textView: TextView = parentView.findViewById(R.id.tvTeamTitle)
        //val imgMore: ImageButton = parentView.findViewById(R.id.ivTeamMore)
        parentBinding.tvTeamTitle.text = parents[groupPosition]
        setIcon(groupPosition)

        setArrow(groupPosition)


        parentBinding.ivTeamMore.setOnTouchListener { v, event ->
            //이미지 버튼 눌림 효과 및 밑에서 다이얼로그 나옴
            val image:ImageButton=v as ImageButton
            if (event?.getAction() == MotionEvent.ACTION_DOWN) {

                image.setBackgroundColor(0xFFD5D2D2.toInt())

            } else if (event?.getAction() == MotionEvent.ACTION_UP) {

                image.setBackgroundColor(0x00000000)
                setBottomSheetDialog(groupPosition)// 다이얼로그 팝업

            }
            return@setOnTouchListener false
        }

        return parentBinding.root

    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parentView: ViewGroup?): View {
        //자식 계층 레이아웃(팀원 계층)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        childBinding= MenuChildBinding.inflate(inflater,parentView,false)
        childBinding.tvUser.text=getChild(groupPosition,childPosition)
        return childBinding.root
    }


    private fun setIcon(parentPosition: Int) //아이콘 설정
    {
        val imageView:ImageView=parentBinding.ivTeam
        imageView.setImageResource(R.drawable.ic_baseline_settings_24)
    }
    private  fun setBottomSheetDialog(parentPosition: Int)
    {
        val bottomSheetDialog: BottomSheetDialog= BottomSheetDialog(parents[parentPosition])
        bottomSheetDialog.show(fragmentManager,"Tag")
    }
    private fun setArrow(parentPosition: Int) {

        if (parentPosition != 0) // TODO: 첫번째는 주지 않음 수정 가능
        {
            parentBinding.ivTeamMore.setImageResource(R.drawable.ic_baseline_more_vert_24)

        }

    }




}