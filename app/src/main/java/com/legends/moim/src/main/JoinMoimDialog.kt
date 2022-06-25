package com.legends.moim.src.main

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import com.legends.moim.R
import com.legends.moim.config.baseModel.Moim

class JoinMoimDialog(context : Context) {

    private val dialog = Dialog(context)

    private lateinit var btnOK: TextView

    private lateinit var moimIdxEt: EditText
    private lateinit var moimPwEt: EditText

    var listener: JoinMoimDialogClickListener? = null

    fun showJoinMoimDialog() {
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_join_moim)
        dialog.setCancelable(false)

        moimIdxEt = dialog.findViewById<EditText>(R.id.dialog_join_moimIdx_et)
        moimPwEt = dialog.findViewById<EditText>(R.id.dialog_join_moimPw_et)

        btnOK = dialog.findViewById(R.id.dialog_join_ok_btn_tv)
        btnOK.setOnClickListener {
            var moimIdx: Int? = null
            if( moimIdxEt.text.isNotEmpty() )
                moimIdx = moimIdxEt.text.toString().toInt()

            var moimPw: String? = null
            if( moimPwEt.text.isNotEmpty() )
                moimPw = moimPwEt.text.toString()

            listener!!.onJoinMoimDialogOKClicked( moimIdx, moimPw )

            dialog.dismiss()
        }
        dialog.show()
    }

    interface JoinMoimDialogClickListener {
        fun onJoinMoimDialogOKClicked( moimIdx: Int?, moimPw: String? )
    }
}