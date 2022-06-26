package com.legends.moim.src.groupMoim.dialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import com.legends.moim.R

class InviteDialog(context : Context, private val moimIdx: Int, private val pw: String?) {

    private val dialog = Dialog(context)

    private lateinit var btnOK: TextView

    private lateinit var moimIdxTv: TextView
    private lateinit var moimPwTv: TextView

//    var listener: InviteDialogClickListener? = null

    fun showInviteDialog() {
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_invite)
        dialog.setCancelable(false)

        moimIdxTv = dialog.findViewById<EditText>(R.id.dialog_invite_moimIdx_tv)
        moimPwTv = dialog.findViewById<EditText>(R.id.dialog_invite_moimPw_tv)

        moimIdxTv.text = moimIdx.toString()
        if(pw != null ) moimPwTv.text = pw
        else moimPwTv.text = "없음"

        btnOK = dialog.findViewById(R.id.dialog_invite_ok_btn_tv)
        btnOK.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

//    interface InviteDialogClickListener {
//        fun onInviteDialogOKClicked()
//    }
}