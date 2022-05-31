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
import com.aminography.primedatepicker.common.OnDayPickedListener
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback
import com.aminography.primedatepicker.picker.theme.LightThemeFactory
import com.google.gson.Gson
import com.legends.moim.R
import com.legends.moim.config.BaseActivity
import com.legends.moim.config.baseModel.DateStructure
import com.legends.moim.databinding.ActivityMakeMoimBinding
import com.legends.moim.src.groupMoim.MoimGroupActivity
import com.legends.moim.src.makeMoim.dialog.SettingDialog
import com.legends.moim.src.makeMoim.dialog.TimeDialog
import com.legends.moim.config.baseModel.Moim
import java.util.*

class MakeMoimActivity: BaseActivity(), TimeDialog.TimeDialogClickListener, SettingDialog.SettingDialogClickListener {

    companion object {
        //var multipleDaysString :String? = null //전역변수로 날짜 데이터 관리.
        var listOfDate = ArrayList<DateStruct>(); //전역변수로 날짜 데이터 관리. DateStruct는 최하단의 클래스 코드 참조.
    }

    lateinit var binding: ActivityMakeMoimBinding

    lateinit var datePicker: PrimeDatePicker

    private val makingMoim = Moim()

    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeMoimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initDatePickerDialog()
        initView()
//        setInitialize()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id) {
            R.id.make_moim_select_date_btn -> {
                initDatePickerDialog()
                datePicker.show(supportFragmentManager, "SOME_TAG")
                //binding.makeMoimSelectDateBtn.text = multipleDaysString
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
        val dummyDayVal1= DateStructure(6, 4, '월')
        val dummyDayVal2= DateStructure(8, 13, '수')
        val dummyDayVal3= DateStructure(11, 4, '목')
        val dummyDayVal4= DateStructure(12, 31, '토')

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

    private fun initView() {
        binding.makeMoimSelectDateBtn.setOnClickListener(this)
        binding.makeMoimSelectTimeBtn.setOnClickListener(this)
        binding.makeMoimSettingTv.setOnClickListener(this)
        binding.makeMoimCompleteBtn.setOnClickListener(this)
    }

    private fun initDatePickerDialog() {

        val today = CivilCalendar()

        val callback = MultipleDaysPickCallback{ multipleDays ->
            //todo 무언가 더 해야 함
            var multipleDaysString = multipleDays.joinToString(" -\n") { it.longDateString }
            //binding.makeMoimSelectDateBtn.text = multipleDaysString
            //Log.d("PickedDates>>>", multipleDays.joinToString(" -\n") { it.longDateString })

            //일단은 여기다 날짜 저장 코드 실행.
            var datestring: String = multipleDaysString

            if (datestring != null && datestring?.length > 0) {
                //var datestring = "수요일, 11 5월 2022\n목요일, 12 5월 2022\n금요일, 13 5월 2022" //더미데이터
                var dateline = datestring.split("\n")
                var selectCount = dateline.count()
                listOfDate = ArrayList<DateStruct>();
                for (i: Int in 0 until selectCount) {
                    var st = StringTokenizer(dateline[i], ",| ")
                    var temp_dayofWeek: String = st.nextToken().toString()
                    var temp_day: Int = st.nextToken().toInt()
                    var temp_month: String = st.nextToken().toString()
                    var temp_year: Int = st.nextToken().toInt()

                    var temp = DateStruct(temp_year, temp_month, temp_day, temp_dayofWeek)
                    listOfDate.add(temp)
                }
                //이건 그냥 검증용. 버튼에 리스트 첫번째 애로 text 바꾸기 해봄.
                var tempDate: String =
                    listOfDate[1].year.toString() + " " + listOfDate[0].month + listOfDate[0].day.toString() + "일, " + listOfDate[0].dayofWeek
                binding.makeMoimSelectDateBtn.text = tempDate
                //여기까지 날짜저장. listOfDate 에 있음.
            }

        }

        datePicker = PrimeDatePicker.dialogWith(today)
            .pickMultipleDays(callback)
            //.initiallyPickedMultipleDays(callback.onMultipleDaysPicked(multipleDays)) 이거 아니다...이건 초기 달력 상태를 선택된거 만드는거..
            //.maxPossibleDate()
            .firstDayOfWeek(Calendar.SUNDAY)
            //.applyTheme(themeFactory) // applyTheme(themeFactory: ThemeFactory)
            .build()

    }

}

data class DateStruct (
    var year : Int,
    var month : String,
    var day : Int,
    var dayofWeek : String
)