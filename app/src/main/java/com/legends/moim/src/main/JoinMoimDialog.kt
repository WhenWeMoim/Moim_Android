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

    private var moimPw: Int = 0

    var listener: JoinMoimDialogClickListener? = null

    fun showJoinMoimDialog() {
        dialog.window?.setBackgroundDrawableResource(android.R.color.darker_gray)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_join_moim)
        dialog.setCancelable(false)

        btnOK = dialog.findViewById(R.id.dialog_time_ok_btn_tv)
        btnOK.setOnClickListener {
            val moimIdx: Int = dialog.findViewById<EditText>(R.id.dialog_join_moimIdx_et).text.toString().toInt()
            val moimPw: Int = dialog.findViewById<EditText>(R.id.dialog_join_moimPw_et).text.toString().toInt()

            listener!!.onJoinMoimDialogOKClicked( moimIdx, moimPw )

            dialog.dismiss()
        }
        dialog.show()
    }

    interface JoinMoimDialogClickListener {
        fun onJoinMoimDialogOKClicked( moimIdx: Int, moimPw: Int )
    }
}