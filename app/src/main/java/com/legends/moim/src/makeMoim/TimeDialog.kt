package com.legends.moim.src.makeMoim

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.view.Window
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.TimePicker
import com.legends.moim.R
import com.legends.moim.databinding.DialogTimeBinding
import com.legends.moim.src.makeMoim.model.makingMoim


@SuppressLint("NewApi")
class TimeDialog(context : Context) {

    val dialog = Dialog(context)

    private lateinit var tvTitle: TextView
    private lateinit var tvMessage: TextView
    private lateinit var btnOK: TextView

    private lateinit var startTimePicker: TimePicker
    private lateinit var endTimePicker: TimePicker

    var listener: TimeDialogClickListener? = null

    fun showTimeDialog() {
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_base)
        dialog.setCancelable(false)

        startTimePicker = dialog.findViewById(R.id.dialog_time_start_tp)
        endTimePicker = dialog.findViewById(R.id.dialog_time_end_tp)

        //todo dialog 설정
        startTimePicker.minute = 0
        endTimePicker.minute = 0
        setTimePickerInterval(startTimePicker)
        setTimePickerInterval(endTimePicker)

        btnOK.setOnClickListener {
            val startTimeHour = startTimePicker.hour
            val endTimeHour = endTimePicker.hour

            listener!!.onTimeDialogOKClicked(startTimeHour, endTimeHour)

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setTimePickerInterval(timePicker: TimePicker) {
        try {
            val TIME_PICKER_INTERVAL = 30
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