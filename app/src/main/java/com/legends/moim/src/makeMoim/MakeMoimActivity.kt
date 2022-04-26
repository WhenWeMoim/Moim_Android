package com.legends.moim.src.makeMoim

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.SparseIntArray
import android.view.View
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.common.BackgroundShapeType
import com.aminography.primedatepicker.common.LabelFormatter
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback
import com.aminography.primedatepicker.picker.theme.LightThemeFactory
import com.legends.moim.R
import com.legends.moim.config.BaseActivity
import com.legends.moim.databinding.ActivityMakeMoimBinding
import com.legends.moim.src.makeMoim.model.makingMoim
import java.util.*

class MakeMoimActivity: BaseActivity(), DateDialog.DateDialogClickListener, TimeDialog.TimeDialogClickListener {

    lateinit var binding: ActivityMakeMoimBinding

    lateinit var datePicker: PrimeDatePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeMoimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initDatePickerDialog()
        initView()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id) {
            R.id.make_moim_select_date_btn -> {
                datePicker.show(supportFragmentManager, "SOME_TAG")
                //showDateDialog()
            }
            R.id.make_moim_select_time_btn -> {
                showTimeDialog()
            }
            R.id.make_moim_setting_tv -> {

            }
            R.id.make_moim_complete_btn -> {
                val intent = Intent()
            }
        }
    }

    fun showTimeDialog() {
        val dig = TimeDialog(this)
        dig.listener = this
        dig.showTimeDialog()
    }

    fun showDateDialog() {
        val dig = DateDialog(this)
        dig.listener = this
        dig.showDateDialog()
    }


    private fun initDatePickerDialog() {

        val themeFactory = object : LightThemeFactory() {

            override val typefacePath: String?
                get() = "fonts/Righteous-Regular.ttf"

            override val dialogBackgroundColor: Int
                get() = getColor(R.color.yellow100)

            override val calendarViewBackgroundColor: Int
                get() = getColor(R.color.yellow100)

            override val pickedDayBackgroundShapeType: BackgroundShapeType
                get() = BackgroundShapeType.ROUND_SQUARE

            override val calendarViewPickedDayBackgroundColor: Int
                get() = getColor(R.color.green800)

            override val calendarViewPickedDayInRangeBackgroundColor: Int
                get() = getColor(R.color.green400)

            override val calendarViewPickedDayInRangeLabelTextColor: Int
                get() = getColor(R.color.gray900)

            override val calendarViewTodayLabelTextColor: Int
                get() = getColor(R.color.purple200)

            override val calendarViewWeekLabelFormatter: LabelFormatter
                get() = { primeCalendar ->
                    when (primeCalendar[Calendar.DAY_OF_WEEK]) {
                        Calendar.SATURDAY,
                        Calendar.SUNDAY -> String.format("%s😍", primeCalendar.weekDayNameShort)
                        else -> String.format("%s", primeCalendar.weekDayNameShort)
                    }
                }

            override val calendarViewWeekLabelTextColors: SparseIntArray
                get() = SparseIntArray(7).apply {
                    val red = getColor(R.color.red300)
                    val indigo = getColor(R.color.indigo500)
                    put(Calendar.SATURDAY, red)
                    put(Calendar.SUNDAY, red)
                    put(Calendar.MONDAY, indigo)
                    put(Calendar.TUESDAY, indigo)
                    put(Calendar.WEDNESDAY, indigo)
                    put(Calendar.THURSDAY, indigo)
                    put(Calendar.FRIDAY, indigo)
                }

            override val calendarViewShowAdjacentMonthDays: Boolean
                get() = true

            override val selectionBarBackgroundColor: Int
                get() = getColor(R.color.brown600)

            override val selectionBarRangeDaysItemBackgroundColor: Int
                get() = getColor(R.color.orange700)
        }

        val today = CivilCalendar()

        val callback = MultipleDaysPickCallback { days ->
            //todo 무언가 더 해야 함
            Log.d("PickedDates>>>", days.toString())
        }

        datePicker = PrimeDatePicker.bottomSheetWith(today)
            .pickMultipleDays(callback)
            //.maxPossibleDate()
            .firstDayOfWeek(Calendar.SUNDAY)
            .applyTheme(themeFactory) // applyTheme(themeFactory: ThemeFactory)
            //.initiallyPickedMultipleDays(pickedDays)
            .build()
    }

    fun initView() {
        binding.makeMoimSelectDateBtn.setOnClickListener(this)
        binding.makeMoimSelectTimeBtn.setOnClickListener(this)
        binding.makeMoimSettingTv.setOnClickListener(this)
        binding.makeMoimCompleteBtn.setOnClickListener(this)
    }

    override fun onDateDialogOKClicked() {
        //TODO("Not yet implemented")
    }

    override fun onTimeDialogOKClicked(startTimeHour: Int, endTimeHour: Int) {
        makingMoim.startTimeHour= startTimeHour
        makingMoim.endTimeHour= endTimeHour

        binding.makeMoimSelectTimeBtn.text = String.format("%d 부터 %d 까지", startTimeHour, endTimeHour)
    }

}