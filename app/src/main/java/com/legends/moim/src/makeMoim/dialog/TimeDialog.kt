package com.legends.moim.src.makeMoim.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.view.Window
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.TimePicker
import com.legends.moim.R
import com.legends.moim.src.makeMoim.model.Moim

class TimeDialog(context : Context, private val makingMoim: Moim) {

    private val dialog = Dialog(context)

    private lateinit var btnOK: TextView

    private lateinit var startTimePicker: NumberPicker
    private lateinit var endTimePicker: NumberPicker

    var listener: TimeDialogClickListener? = null

    fun showTimeDialog() {
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_time)
        dialog.setCancelable(false)

        startTimePicker = dialog.findViewById(R.id.dialog_time_start_np)
        endTimePicker = dialog.findViewById(R.id.dialog_time_end_np)

        initNumberPicker(makingMoim)

        btnOK = dialog.findViewById(R.id.dialog_time_ok_btn_tv)
        btnOK.setOnClickListener {
            val startTimeHour = startTimePicker.value
            val endTimeHour = endTimePicker.value

            listener!!.onTimeDialogOKClicked(startTimeHour, endTimeHour)

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun initNumberPicker(makingMoim: Moim) {
        //startTimePicker
        startTimePicker.minValue = 0
        startTimePicker.maxValue = 24
        startTimePicker.setOnValueChangedListener { numberPicker, oldVal, newVal ->
            if ( endTimePicker.value <= newVal ) endTimePicker.value = newVal + 1
        }

        //endTimePicker
        endTimePicker.minValue = 0
        endTimePicker.maxValue = 24
        endTimePicker.value = 18
        endTimePicker.setOnValueChangedListener { numberPicker, oldVal, newVal ->
            if ( startTimePicker.value >= newVal ) startTimePicker.value = newVal - 1
        }

        //init Val
        startTimePicker.value = makingMoim.startTimeHour
        endTimePicker.value = makingMoim.endTimeHour
    }

    interface TimeDialogClickListener {
        fun onTimeDialogOKClicked( startTimeHour: Int, endTimeHour: Int )
    }
}