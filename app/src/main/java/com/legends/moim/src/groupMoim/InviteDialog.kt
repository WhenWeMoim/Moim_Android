package com.legends.moim.src.groupMoim

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import com.legends.moim.R
import com.legends.moim.config.baseModel.Moim

class InviteDialog(context : Context, val moimIdx: Int, val pw: String) {

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
        moimPwTv.text = pw

        btnOK = dialog.findViewById(R.id.dialog_join_ok_btn_tv)
        btnOK.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

//    interface InviteDialogClickListener {
//        fun onInviteDialogOKClicked()
//    }
}