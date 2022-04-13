package com.legends.moim.src.makeMoim

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.TextView
import android.widget.TimePicker
import com.legends.moim.R
import com.legends.moim.databinding.DialogTimeBinding

class TimeDialog(context : Context) {

    val dialog = Dialog(context)

    lateinit var binding : DialogTimeBinding

    private lateinit var tvTitle: TextView
    private lateinit var tvMessage: TextView
    private lateinit var btnOK: TextView

    private lateinit var startTimePicker: TimePicker
    private lateinit var endTimePicker: TimePicker
//    private lateinit var startTime: Int
//    private lateinit var startMin: Int
//    private lateinit var endTime: Int
//    private lateinit var endMin: Int


    var listener: BaseDialogClickListener? = null

    open fun show(title: String, message: String, okMessage: String) {
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_base)
        dialog.setCancelable(false)

        tvTitle = dialog.findViewById(R.id.dialog_base_title_tv)
        tvTitle.text = title

        tvMessage = dialog.findViewById(R.id.dialog_base_message_tv)
        tvMessage.text = message

        btnOK = dialog.findViewById(R.id.dialog_base_ok_btn_tv)
        btnOK.text = okMessage
        btnOK.setOnClickListener {
            listener!!.onOKClicked()

            dialog.dismiss()
        }

        startTimePicker = dialog.findViewById(R.id.dialog_time_start_tp)
        endTimePicker = dialog.findViewById(R.id.dialog_time_end_tp)

        
        startTimePicker.setOnTimeChangedListener()
        //todo dialog 설정
        startTimePicker.minute = 0


        dialog.show()
    }

    open fun getStartTime(): Int = startTimePicker.hour
    open fun getEndTime(): Int = endTimePicker.hour

    interface BaseDialogClickListener {
        fun onOKClicked()
//        fun onCancelClicked()
    }
}

private fun TimePicker.setOnTimeChangedListener() {
    TODO("Not yet implemented")
}
