package com.legends.moim.src.makeMoim

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback
import com.google.gson.Gson
import com.legends.moim.R
import com.legends.moim.config.BaseActivity
import com.legends.moim.config.BaseDialog2
import com.legends.moim.config.baseModel.DateStruct
import com.legends.moim.config.baseModel.Moim
import com.legends.moim.databinding.ActivityMakeMoimBinding
import com.legends.moim.src.groupMoim.GroupMoimActivity
import com.legends.moim.src.makeMoim.dialog.SettingDialog
import com.legends.moim.src.makeMoim.dialog.TimeDialog
import com.legends.moim.src.makeMoim.model.PostMoimReq
import com.legends.moim.utils.FLAG_ACTIVITY_MAKEMOIM
import com.legends.moim.utils.dateStructure2Int
import com.legends.moim.utils.getUserIdx
import com.legends.moim.utils.retrofit.PostMoimView
import com.legends.moim.utils.retrofit.RetrofitService
import java.util.*

class MakeMoimActivity: BaseActivity(),TimeDialog.TimeDialogClickListener, SettingDialog.SettingDialogClickListener,
    PostMoimView {

    companion object {
        var dates = ArrayList<DateStruct>() //전역변수로 날짜 데이터 관리. DateStruct는 최하단의 클래스 코드 참조.
    }

    private val makingMoim = Moim()

    lateinit var binding: ActivityMakeMoimBinding
    lateinit var datePicker: PrimeDatePicker
    private lateinit var dateLayout: GridLayout
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeMoimBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dateLayout = binding.makeMoimSelectDateLayout
        dates.clear()

        initDatePickerDialog()
        initView()
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        val dlg = BaseDialog2(this)
        dlg.listener = CancleDialog()
        dlg.showCancleDialog("모임 생성 취소", "모임 생성을 취소하시겠습니까?\n입력된 정보는 사라집니다.", "확인")
    }

    inner class CancleDialog(): BaseDialog2.BaseDialogClickListener {
        override fun onOKClicked() {
            finish()
        }
    }

    override fun onOKClicked() {
        super.onOKClicked()
        finish()
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

                if( dates.isEmpty() ) {
                    showDialog("모임 생성 오류", "날짜가 입력되지 않았습니다.", "확인", null)
                    return
                }
                getMakingMoimInfo()

                //todo postMoim
                postMoim( makingMoim )
            }
        }
    }

    private fun getMakingMoimInfo() {
        if (binding.makeMoimTitleEt.text.isNotEmpty())
            makingMoim.moimTitle = binding.makeMoimTitleEt.text.toString()
        if (binding.makeMoimExplainEt.text.isNotEmpty())
            makingMoim.moimDescription = binding.makeMoimExplainEt.text.toString()

        makingMoim.dates = dates
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
        binding.makeMoimSelectTimeBtn.text = String.format("%d시 부터 %d시 까지", makingMoim.startTimeHour, makingMoim.endTimeHour)

        binding.makeMoimSelectDateBtn.setOnClickListener(this)
        binding.makeMoimSelectTimeBtn.setOnClickListener(this)
        binding.makeMoimSettingTv.setOnClickListener(this)
        binding.makeMoimCompleteBtn.setOnClickListener(this)
    }

    private fun initDatePickerDialog() {

        dates.clear()

        val today = CivilCalendar()

        val callback = MultipleDaysPickCallback{ multipleDays ->
            val multipleDaysString = multipleDays.joinToString(" -\n") { it.longDateString }
            //binding.makeMoimSelectDateBtn.text = multipleDaysString
            //Log.d("PickedDates>>>", multipleDays.joinToString(" -\n") { it.longDateString })

            //일단은 여기다 날짜 저장 코드 실행.
            val dateString: String? = multipleDaysString

            if ( dateString != null && dateString.isNotEmpty() ) {
                //var datestring = "수요일, 11 5월 2022\n목요일, 12 5월 2022\n금요일, 13 5월 2022" //더미데이터
                val dateline = dateString.split("\n")
                val selectCount = dateline.count()
                dates = ArrayList<DateStruct>()
                for ( i: Int in 0 until selectCount ) {
                    val st = StringTokenizer(dateline[i], ",| ")
                    val temp_dayOfWeek: String = st.nextToken()[0].toString()
                    val temp_day: Int = st.nextToken().toInt()
                    val temp_month: Int = st.nextToken().toString()[0]-'0'
                    val temp_year: Int = st.nextToken().toInt()

                    var temp = DateStruct(temp_year, temp_month, temp_day, temp_dayOfWeek)

                    if(i==0)
                        dates.add(temp)
                    //add 전에 순서 따지고 넣기.

                    for(j: Int in dates.size-1 downTo 0){
                        if(temp.year>dates[j].year){
                            dates.add(j+1, temp)
                            break
                        }
                        else if(temp.year==dates[j].year){
                            if(temp.month>dates[j].month){
                                dates.add(j+1, temp)
                                break
                            }
                            else if(temp.month==dates[j].month){
                                if(temp.day>dates[j].day){
                                    dates.add(j+1, temp)
                                    break
                                }
                            }
                        }

                        if(j==0 && i!=0){
                            dates.add(0, temp)
                        }
                    }
                }
                Log.d("dateSelectTest>>>>>", dates.toString())
                //이건 그냥 검증용. 버튼에 리스트 첫번째 애로 text 바꾸기 해봄.

                if( dates.size <= 0 ) {
                    binding.makeMoimSelectDateBtn.visibility = View.VISIBLE
                    binding.makeMoimSelectDateLayout.visibility = View.GONE
                    binding.makeMoimNumDatesTv.visibility = View.INVISIBLE
                } else {
                    binding.makeMoimSelectDateBtn.visibility = View.GONE
                    binding.makeMoimSelectDateLayout.visibility = View.VISIBLE
                    binding.makeMoimNumDatesTv.visibility = View.VISIBLE
                    binding.makeMoimNumDatesTv.text = String.format(" 총 %d개 선택", dates.size)

                    binding.makeMoimSelectDateLayout.removeAllViews()
                    for ( i: Int in dates.indices ) {
                        if( i >= 6 ) break

                        val tempDateString: String =
                            dates[i].month.toString() + "월 "+ dates[i].day.toString() + "일"

                        val dateBtnPm = GridLayout.LayoutParams()
                            dateBtnPm.setMargins(4, 4, 4, 4)

                        val newDateBtn = AppCompatButton(this)
                            newDateBtn.background = resources.getDrawable(R.drawable.bg_makemoim_date_stroke_btn, null)
                            newDateBtn.layoutParams = dateBtnPm
                            newDateBtn.text = tempDateString
                            newDateBtn.textSize = 14f
                            newDateBtn.includeFontPadding = false
                            newDateBtn.setOnClickListener(DateClickListener())

                        binding.makeMoimSelectDateLayout.addView(newDateBtn)
                    }
                }

//                var tempDate: String =
//                    listOfDate[0].year.toString() + " " + listOfDate[0].month.toString() + "월, "+ listOfDate[0].day.toString() + "일, " + listOfDate[0].dayOfWeek.toString()
//                binding.makeMoimSelectDateBtn.text = tempDate
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

    private inner class DateClickListener: View.OnClickListener {
        override fun onClick(v: View?) {
            initDatePickerDialog()
            datePicker.show(supportFragmentManager, "SOME_TAG")
        }
    }

    private fun postMoim(moim: Moim) {
        val dateIntArray = dateStructure2Int( moim.dates )

        val serverMoimStruct = PostMoimReq(
            userIdx = getUserIdx(),
            moimTitle = moim.moimTitle,
            moimDescription = moim.moimDescription,
            startTime = moim.startTimeHour,
            endTime = moim.endTimeHour,
            dates = dateIntArray
        )

        val retrofitService = RetrofitService()
        retrofitService.setPostMoimView(this)

        Log.d("postMoim Active>>>", "sending Model : $serverMoimStruct")
        retrofitService.postMoim(serverMoimStruct)
    }

    override fun onPostMoimLoading() {
        //todo Loading Effect
    }

    override fun onPostMoimSuccess(moimIdx: Int, pw: String) {
        Toast.makeText(this, "모임 생성 성공.", Toast.LENGTH_SHORT).show()
        makingMoim.moimIdx =  moimIdx
        makingMoim.password = pw

        startMoimGroupActivity( makingMoim )
    }

    override fun onPostMoimFailure(code: Int, message: String) {
        Toast.makeText(this, "모임 생성 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
    }

    private fun startMoimGroupActivity(moim: Moim) {
        val intent = Intent(this, GroupMoimActivity::class.java)
        intent.putExtra("startActivityFlag", FLAG_ACTIVITY_MAKEMOIM)
        intent.putExtra("moimInfo", gson.toJson(moim))
        startActivity(intent)
        finish()
    }
}