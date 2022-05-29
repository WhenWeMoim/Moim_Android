package com.legends.moim.src.makeMoim

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.SparseIntArray
import android.view.View
import android.widget.Toast
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.common.BackgroundShapeType
import com.aminography.primedatepicker.common.LabelFormatter
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback
import com.aminography.primedatepicker.picker.theme.LightThemeFactory
import com.google.gson.Gson
import com.legends.moim.R
import com.legends.moim.config.BaseActivity
import com.legends.moim.databinding.ActivityMakeMoimBinding
import com.legends.moim.src.groupMoim.MoimGroupActivity
import com.legends.moim.src.makeMoim.dialog.SettingDialog
import com.legends.moim.src.makeMoim.dialog.TimeDialog
import com.legends.moim.src.makeMoim.model.Moim
import com.legends.moim.src.makeMoim.model.SelectedDate
import java.util.*

class MakeMoimActivity: BaseActivity(), TimeDialog.TimeDialogClickListener, SettingDialog.SettingDialogClickListener {

    lateinit var binding: ActivityMakeMoimBinding

    lateinit var datePicker: PrimeDatePicker

    private val makingMoim = Moim()

    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeMoimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initDatePickerDialog()
        setInitialize()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id) {
            R.id.make_moim_select_date_btn -> {
                datePicker.show(supportFragmentManager, "SOME_TAG")
                //showDateDialog()
            }
            R.id.make_moim_select_time_btn -> {
                showTimeDialog(makingMoim)
            }
            R.id.make_moim_setting_tv -> {
                showSettingDialog()
            }
            R.id.make_moim_complete_btn -> {
                getMakingMoimInfo()

                val intent = Intent(this, MoimGroupActivity::class.java)
                intent.putExtra("moimInfo", gson.toJson(makingMoim))
                startActivity(intent)
                finish()
            }
        }
    }

    private fun getMakingMoimInfo() {
        if( binding.makeMoimTitleEt.text.isNotEmpty() )
            makingMoim.title = binding.makeMoimTitleEt.text.toString()
        if( binding.makeMoimExplainEt.text.isNotEmpty() )
            makingMoim.explain = binding.makeMoimExplainEt.text.toString()
        //testValue 삽입 함수
        addTestDummyData()
    }

    private fun addTestDummyData() {
        val dummyDayVal1: String = "2022-06-14"
        val dummyDayVal2: String = "2022-06-15"
        val dummyDayVal3: String = "2022-06-18"
        val dummyDayVal4: String = "2022-06-24"

        makingMoim.dates.add(dummyDayVal1)
        makingMoim.dates.add(dummyDayVal2)
        makingMoim.dates.add(dummyDayVal3)
        makingMoim.dates.add(dummyDayVal4)
    }

    private fun showTimeDialog(makingMoim: Moim) {
        val dig = TimeDialog(this, makingMoim)
        dig.listener = this
        dig.showTimeDialog()
    }

    private fun showSettingDialog() {
        val dig = SettingDialog(this)
        dig.listener = this
        dig.showSettingDialog()
    }

    /*------- interface override fun -------*/

    override fun onTimeDialogOKClicked(startTimeHour: Int, endTimeHour: Int) {
        makingMoim.startTimeHour = startTimeHour
        makingMoim.endTimeHour = endTimeHour

        binding.makeMoimSelectTimeBtn.text = String.format("%d시 부터 %d시 까지", startTimeHour, endTimeHour)
    }

    override fun onSettingDialogOKClicked() {
        //아직까진 아무것도 안함
    }

    /*------- initial -------*/

    private fun setInitialize() {
        binding.makeMoimSelectDateBtn.setOnClickListener(this)
        binding.makeMoimSelectTimeBtn.setOnClickListener(this)
        binding.makeMoimSettingTv.setOnClickListener(this)
        binding.makeMoimCompleteBtn.setOnClickListener(this)

        binding.makeMoimExplainEt.addTextChangedListener(object : TextWatcher {
            val wordCountTv = binding.makeMoimTextCountTv
            var userInput = binding.makeMoimExplainEt

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                wordCountTv.text = "0 / 200"
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                wordCountTv.text = "${userInput.length()} / 200"
            }

            override fun afterTextChanged(s: Editable?) {
                if (userInput.isFocused && userInput.length() > 200) {
                    userInput.setText(s.toString().substring(0, 200))
                    userInput.setSelection(s!!.length - 1)
                    Toast.makeText(this@MakeMoimActivity,
                        "200자까지 입력 가능합니다.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun initDatePickerDialog() {

        val themeFactory = object : LightThemeFactory() {

//            override val typefacePath: String?
//                get() = "font/notosans_kr_default.otf"

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

}