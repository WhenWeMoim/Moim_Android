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


@SuppressLint("NewApi")
class TimeDialog(context : Context) {

    private val dialog = Dialog(context)

//    private lateinit var tvTitle: TextView
//    private lateinit var tvMessage: TextView
    private lateinit var btnOK: TextView

    private lateinit var startTimePicker: TimePicker
    private lateinit var endTimePicker: TimePicker

    var listener: TimeDialogClickListener? = null

    fun showTimeDialog() {
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_time)
        dialog.setCancelable(false)

//        tvTitle = dialog.findViewById(R.id.dialog_time_title_tv)
//        tvMessage = dialog.findViewById(R.id.dialog_time_ok_btn_tv)

        startTimePicker = dialog.findViewById(R.id.dialog_time_start_tp)
        endTimePicker = dialog.findViewById(R.id.dialog_time_end_tp)

        initTimePicker()

        btnOK = dialog.findViewById(R.id.dialog_time_ok_btn_tv)
        btnOK.setOnClickListener {
            val startTimeHour = startTimePicker.hour
            val endTimeHour = endTimePicker.hour

            listener!!.onTimeDialogOKClicked(startTimeHour, endTimeHour)

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun initTimePicker() {
        startTimePicker.hour = 9
        endTimePicker.hour = 18

        startTimePicker.minute = 0
        endTimePicker.minute = 0

        setTimePickerInterval(startTimePicker)
        setTimePickerInterval(endTimePicker)

        startTimePicker.setOnTimeChangedListener { timePicker, currentHour, currentMin ->
            if ( endTimePicker.hour <= currentHour ) startTimePicker.hour = currentHour + 1
        }
        endTimePicker.setOnTimeChangedListener { timePicker, currentHour, currentMin ->
            if ( startTimePicker.hour >= currentHour ) startTimePicker.hour = currentHour - 1
        }
    }

    private fun setTimePickerInterval(timePicker: TimePicker) {
        try {
            val TIME_PICKER_INTERVAL = 60
            val minutePicker = timePicker.findViewById(
                Resources.getSystem().getIdentifier(
                    "minute", "id", "android"
                )
            ) as NumberPicker
            minutePicker.minValue = 0
            minutePicker.maxValue = 60 / TIME_PICKER_INTERVAL - 1
            val displayedValues: MutableList<String> = ArrayList()
            var i = 0
            while (i < 60) {
                displayedValues.add(String.format("%02d", i))
                i += TIME_PICKER_INTERVAL
            }
            minutePicker.displayedValues = displayedValues.toTypedArray()
        } catch (e: Exception) {
        }
    }

    interface TimeDialogClickListener {
        fun onTimeDialogOKClicked( startTimeHour: Int, endTimeHour: Int )
//        fun onCancelClicked()
    }
}