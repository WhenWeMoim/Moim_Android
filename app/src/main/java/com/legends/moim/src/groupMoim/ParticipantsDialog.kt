package com.legends.moim.src.groupMoim

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.TextView
import com.legends.moim.R

class ParticipantsDialog(context : Context) {

    val dialog = Dialog(context)
    private lateinit var tvTitle: TextView
    lateinit var tvMessage: TextView
    private lateinit var btnOK: TextView

    fun showTimeDialog() {
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_participants)
        dialog.setCancelable(false)

        tvTitle.text = "모임 참가자 명단"
        tvMessage.text = "박재형" + ", " + "박민우"

        //btnOK = dialog.findViewById(R.id.dialog_base_ok_btn_tv)

        btnOK.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}