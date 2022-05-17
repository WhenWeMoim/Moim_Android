package com.legends.moim.src.makeMoim

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.TextView
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.legends.moim.R
import java.util.*

class DateDialog(con:Context) {

    val con1 = con

    var dateString = ""
    var timeString = ""
    lateinit var result: TextView
    lateinit var selectedDate: Calendar
/*    private lateinit var tvTitle: TextView
    private lateinit var tvMessage: TextView
    private lateinit var btnOK: TextView
    private lateinit var datePicker : PrimeDatePicker

    var listener: DateDialogClickListener? = null
*/
    fun showDateDialog() {

        val cal = Calendar.getInstance()    //캘린더뷰 만들기
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            dateString = "${year}년 ${month+1}월 ${dayOfMonth}일"
            result.text = "날짜/시간 : "+dateString + " / " + timeString
            selectedDate = Calendar.getInstance().apply { set(year, month, dayOfMonth) }
        }
        var dpd= DatePickerDialog(con1, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH))

        dpd.show()
        /*
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

         */

//        dialog.show()
    }
/*
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

//        dialog.show()
    }*/

    interface DateDialogClickListener {
        fun onDateDialogOKClicked()
//        fun onCancelClicked()
    }
}