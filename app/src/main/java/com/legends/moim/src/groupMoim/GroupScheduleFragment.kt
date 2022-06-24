package com.legends.moim.src.groupMoim

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import com.legends.moim.R
import com.legends.moim.config.baseModel.Moim
import com.legends.moim.src.groupMoim.model.selectedBtnFunc
import java.lang.StringBuilder

/**
 * 한 개 행(row) : 한 시간대
 * 한 개 열(column) : 하루
 * 배열은 두개가 들어감. 관리를 위한 실제 버튼 배열(buttons), 스케줄 결과가 들어가는 배열(resultSchedule)
 */

class GroupScheduleFragment(private val moim: Moim): Fragment() {

    private var numOfDays = 7 //임시데이터 -> todo 행 수 = 선택한 날짜 개수 makingMoim.Date.size
    private var numOfTimes = 12 //makingMoim.endTimeHour - makingMoim.startTimeHour //열 수 = 시간 구간 개수

    private lateinit var dateLayout: LinearLayout
    private lateinit var timeLayout: LinearLayout
    private lateinit var scheduleLayout: TableLayout

    private lateinit var timeRows: Array<TableRow>
    private lateinit var scheduleButtons: Array<Array<Button>>

    private lateinit var scheduleData: Array<IntArray>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val v: View = inflater.inflate(R.layout.fragment_schedule_group, container, false)

        getData()

        initScheduleTable(v)

        return v
    }

    private fun getData() {
        val interval: Int = moim.endTimeHour - moim.startTimeHour
        if( interval <= 0 ) {
            Toast.makeText(context, "시간 시간표 생성 중 오류가 발생했습니다. 다시 시도해주세요", Toast.LENGTH_LONG).show()
            return
        }
        numOfTimes = interval

        if( moim.dates.isEmpty() ) {
            Toast.makeText(context, "날짜 시간표 생성 중 오류가 발생했습니다. 다시 시도해주세요", Toast.LENGTH_LONG).show()
            return
        }
        numOfDays = moim.dates.size

        //todo Group용 scheduleData 초기화 필요
        //scheduleData = Array(size = numOfDays, init = { IntArray( size = numOfTimes, init = { 2 } ) } )
    }

    private fun initScheduleTable(v: View) {

        //time 시간표 삽입
        timeLayout = v.findViewById(R.id.group_schedule_time_linearLayout) as LinearLayout

        val timeTextPm = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1F)
        timeTextPm.gravity = Gravity.RIGHT

        lateinit var timeText: TextView

        var i: Int = 0

        timeText = TextView(context)
        timeText.text = String.format("")
        timeText.textSize = 44.197F
        timeLayout.addView(timeText)

        while( i <= numOfTimes ) {
            timeText = TextView(context)
            timeText.layoutParams = timeTextPm
            timeText.text = String.format("%d시", moim.startTimeHour + i )
            timeText.textSize = 10F
            timeText.setTextColor(getColor(requireActivity().applicationContext, R.color.black))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                timeText.typeface = resources.getFont(R.font.notosans_kr_regular)
            }
            timeText.includeFontPadding = false

            timeLayout.addView(timeText)
            timeText = TextView(context)
            timeText.text = String.format("")
            timeText.textSize = 24.619F
            timeLayout.addView(timeText)
            i++
        }

        //date 시간표 삽입
        dateLayout = v.findViewById(R.id.group_schedule_date_linearLayout) as LinearLayout

        val dateTextPm = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1F)
            dateTextPm.gravity = Gravity.CENTER
        lateinit var dateText: TextView

        i = 0
        while( i < numOfDays ) {
            dateText = TextView(context)
            dateText.layoutParams = dateTextPm
//            dateText.text = String.format("%d월\n%d일\n(%c)", moim.dates[i].month, moim.dates[i].day, moim.dates[i].dayOfWeek)
            dateText.text = String.format("${moim.dates[i].month}월\n${moim.dates[i].day}\n(${moim.dates[i].dayOfWeek})")

            dateText.setTextColor(getColor(requireActivity().applicationContext, R.color.moim_main_1))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dateText.typeface = resources.getFont(R.font.notosans_kr_regular)
            }
            dateText.includeFontPadding = false
            dateText.gravity = Gravity.CENTER
            dateText.textSize = 10F

            dateLayout.addView(dateText)

            i++
        }

        scheduleLayout = v.findViewById(R.id.group_schedule_tableLayout) as TableLayout

        timeRows = Array<TableRow>(size = numOfTimes, init = { TableRow(v.context) } ) //한개 행 = 날짜 개수
        scheduleButtons = Array<Array<Button>>(size = numOfTimes,
            init = { Array<Button>(size = numOfDays, init = {Button(v.context)} ) }
        )
//        scheduleButtons = Array<Array<Button>>(size = numOfTimes,
//            init = { Array<Button>(size = numOfDays, init = {Button(v.context, resources.getXml(R.drawable.bg_schedule_cell_btn2))} ) }
//        )

        val rowPm: TableLayout.LayoutParams = TableLayout.LayoutParams(0, 40, 1F)
        val cellPm: TableRow.LayoutParams =
            TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1F)
//        val cellPm: TableRow.LayoutParams =
//            TableRow.LayoutParams(30, 40)

        i = 0
        var j: Int
        //임시로 모임 schedules String 정의.
        //todo schedules 스케줄데이터 가져오기.
        var sb = StringBuilder()
        for(temp_i:Int in 0 until numOfTimes*numOfDays){
            sb.append(((temp_i%4)+1).toString())
        }
        //Log.d("SBCheckPoint : ", sb.toString())
        var dummySchedulesData: String = sb.toString() //"111222333444"
        var scheduleCount: Int = 0

        while ( i < numOfTimes ) {
            //TableRow 생성
            timeRows[i] = TableRow(v.context)
            timeRows[i].layoutParams = rowPm
            scheduleLayout.addView(timeRows[i])

            j = 0
            while ( j < numOfDays ) {
                //TableRow 안에 Button(cell) 생성
                scheduleButtons[i][j].setBackgroundResource(R.drawable.bg_schedule_cell_btn2)
                scheduleButtons[i][j].layoutParams = cellPm

//                cellButtons[i][j]?.setOnClickListener(CellClickListener(scheduleResult, i, j))
                timeRows[i].addView(scheduleButtons[i][j])
                j += 1
            }
            i += 1

        }
        //여기서 모임 시간표 색깔 삽입.
        j = 0
        while ( j < numOfDays ){
            i = 0
            while ( i < numOfTimes ) {
                var color = dummySchedulesData[scheduleCount]-'0'
                //Log.d("SBCheckPoint : ", dummySchedulesData[scheduleCount].toString())
                moimSceduleColor(scheduleButtons[i][j], color)
                scheduleCount += 1
                i += 1
            }
            j += 1
        }
    }

    private fun moimSceduleColor(scheduleButtonsView: Button, color: Int){
        //todo 여기서 숫자 몇에 어떤 색깔 쓸지 수정.
        when(color) {
            0 -> {
                //default -> 아무것도 선택하지 않았을때, 생략
            }
            1 -> {
                scheduleButtonsView!!.setBackgroundResource(R.drawable.bg_schedule_cell_choice1_like_btn)
            }
            2 -> {
                scheduleButtonsView!!.setBackgroundResource(R.drawable.bg_schedule_cell_choice2_possible_btn)
            }
            3 -> {
                scheduleButtonsView!!.setBackgroundResource(R.drawable.bg_schedule_cell_choice3_dislike_btn)
            }
            4 -> {
                scheduleButtonsView!!.setBackgroundResource(R.drawable.bg_schedule_cell_choice4_impossible_btn)
            }
        }

    }
}