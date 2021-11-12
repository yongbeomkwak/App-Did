package com.example.appdid.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class TeamMemberHeightDecoration(private val hegiht:Int) :RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        //아이템 별 높이 설정
        if(parent.getChildAdapterPosition(view)!=parent.adapter!!.itemCount-1)
        {
            outRect.bottom=hegiht
        }

    }
}