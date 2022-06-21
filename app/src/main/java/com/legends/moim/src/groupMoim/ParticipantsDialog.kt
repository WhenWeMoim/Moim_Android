package com.legends.moim.src.groupMoim

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.GridLayout
import android.widget.TextView
import com.legends.moim.R

class ParticipantsDialog(context : Context) {

    private val dialog = Dialog(context)
    private lateinit var tvTitle: TextView
    private lateinit var tvMessage: TextView
    private lateinit var btnOK: TextView

    private lateinit var participantsLayout: GridLayout

    fun showTimeDialog() {
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_participants)
        dialog.setCancelable(false)

        tvTitle = dialog.findViewById(R.id.dialog_participants_title_tv)
        tvMessage = dialog.findViewById(R.id.dialog_participants_message_tv)

        btnOK = dialog.findViewById(R.id.dialog_participants_ok_btn_tv)

        participantsLayout = dialog.findViewById(R.id.dialog_participants_gridLayout)

        tvTitle.text = "모임 참가자 명단"
        tvMessage.text = "박재형" + ", " + "박민우"

        //btnOK = dialog.findViewById(R.id.dialog_base_ok_btn_tv)

        btnOK.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}