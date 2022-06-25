package com.legends.moim.src.groupMoim

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.GridLayout
import android.widget.TextView
import com.legends.moim.R
import com.legends.moim.src.groupMoim.model.UserSchedules

class ParticipantsDialog(context : Context) {

    private val dialog = Dialog(context)
    private lateinit var tvTitle: TextView
    private lateinit var tvMessage: TextView
    private lateinit var btnOK: TextView

    private lateinit var participantsLayout: GridLayout

    fun showTimeDialog(userSchedules: Array<UserSchedules>?) {
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_participants)
        dialog.setCancelable(false)

        tvTitle = dialog.findViewById(R.id.dialog_participants_title_tv)
        tvMessage = dialog.findViewById(R.id.dialog_participants_message_tv)

        btnOK = dialog.findViewById(R.id.dialog_participants_ok_btn_tv)

        participantsLayout = dialog.findViewById(R.id.dialog_participants_gridLayout)

        tvTitle.text = "참가중인 인원" //모임 참가자 명단

        var resultList = ""

        if(userSchedules != null) {
            for( i:Int in userSchedules.indices ) {
                resultList += userSchedules[i].userName
                if(i == userSchedules.size-1) break
                resultList += ", "
            }
        }

        tvMessage.text = resultList //임시 참가자 데이터. 그리드 레이아웃으로 구현할 생각 해야할지도, 아니면 String 집합으로 그냥?

        //btnOK = dialog.findViewById(R.id.dialog_base_ok_btn_tv)

        btnOK.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}