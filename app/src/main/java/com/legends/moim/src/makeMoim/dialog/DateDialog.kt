package com.legends.moim.src.makeMoim.dialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.TextView
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.legends.moim.R

class DateDialog(context : Context) {

    val dialog = Dialog(context)

    private lateinit var tvTitle: TextView
    private lateinit var tvMessage: TextView
    private lateinit var btnOK: TextView
    private lateinit var datePicker : PrimeDatePicker

    var listener: DateDialogClickListener? = null

    fun showDateDialog() {
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_base)
        dialog.setCancelable(false)
        datePicker = dialog.findViewById(R.id.dialog_date_calender_cv)
        btnOK = dialog.findViewById(R.id.dialog_base_ok_btn_tv)

        val today = CivilCalendar()

        datePicker = PrimeDatePicker.dialogWith(today)
            .pickMultipleDays()
            .build()

        btnOK.setOnClickListener {
            listener!!.onDateDialogOKClicked()
            dialog.dismiss()
        }

        dialog.show()
    }

    fun showDateDialog(title: String, message: String, okMessage: String) {
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

            listener!!.onDateDialogOKClicked()

            dialog.dismiss()
        }

//        btnCancel = dialog.findViewById(R.id.dialog_base_close_btn_iv)
//        btnCancel.setOnClickListener {
//            listener!!.onCancelClicked()
//            dialog.dismiss()
//        }

        dialog.show()
    }

    interface DateDialogClickListener {


        fun onDateDialogOKClicked()
//        fun onCancelClicked()
    }
}