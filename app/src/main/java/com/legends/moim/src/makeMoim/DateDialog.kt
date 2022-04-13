package com.legends.moim.src.makeMoim

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.datepicker.MaterialCalendar
import com.legends.moim.R

class DateDialog(context : Context) {

    val dialog = Dialog(context)

    private lateinit var tvTitle: TextView
    private lateinit var tvMessage: TextView
    private lateinit var btnOK: TextView
    private lateinit var calendar : CalendarView

    var listener: BaseDialogClickListener? = null

    open fun showDateDialog(title: String, message: String, okMessage: String) {
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

//        btnCancel = dialog.findViewById(R.id.dialog_base_close_btn_iv)
//        btnCancel.setOnClickListener {
//            listener!!.onCancelClicked()
//            dialog.dismiss()
//        }

        dialog.show()
    }

    interface BaseDialogClickListener {
        fun onOKClicked()
//        fun onCancelClicked()
    }
}