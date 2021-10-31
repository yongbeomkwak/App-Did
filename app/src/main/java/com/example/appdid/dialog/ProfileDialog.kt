package com.example.appdid.dialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import com.example.appdid.R

class ProfileDialog (val mcontext:Context)  {

    lateinit var dialog:Dialog

    fun callDialog()
    {
        dialog= Dialog(mcontext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) //제목  없음
        dialog.setContentView(R.layout.dialog_change_profile_photo)
        var params:WindowManager.LayoutParams= dialog.window!!.attributes
        params.width=WindowManager.LayoutParams.MATCH_PARENT
        params.height=WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes=params
        dialog.show()
    }

}