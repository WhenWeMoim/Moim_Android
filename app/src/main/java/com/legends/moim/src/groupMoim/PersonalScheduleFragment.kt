package com.legends.moim.src.groupMoim

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.legends.moim.R
import com.legends.moim.databinding.FragmentSchedulePersonalBinding

/**
 * 한 개 행(row) : 한 시간대
 * 한 개 열(column) : 하루
 * 배열은 두개가 들어감. 관리를 위한 실제 버튼 배열(buttons), 스케줄 결과가 들어가는 배열(resultSchedule)
 */

class PersonalScheduleFragment: Fragment() {

    private var numOfDays = 7 //임시데이터 -> todo 행 수 = 선택한 날짜 개수 makingMoim.Date.size
    private var numOfTimes = 12 //임시데이터 -> todo makingMoim.endTimeHour - makingMoim.startTimeHour //열 수 = 시간 구간 개수

    private lateinit var dateLayout: LinearLayout
    private lateinit var timeLayout: LinearLayout
    private lateinit var scheduleLayout: TableLayout

    private lateinit var timeRows: Array<TableRow> //한개 행(row)
    private lateinit var scheduleButtons: Array<Array<Button>>

    private lateinit var scheduleData: Array<IntArray>

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val v: View = inflater.inflate(R.layout.fragment_schedule_personal, container, false)

        getData()

        initScheduleTable(v)

        return v
    }

    private fun getData() {
        val interval: Int = thisMoim.endTimeHour - thisMoim.startTimeHour
        if( interval <= 0 ) {
            Toast.makeText(context, "시간 시간표 생성 중 오류가 발생했습니다. 다시 시도해주세요", Toast.LENGTH_LONG).show()
            return
        }
        numOfTimes = interval

        if( thisMoim.dates.isEmpty() ) {
            Toast.makeText(context, "날짜 시간표 생성 중 오류가 발생했습니다. 다시 시도해주세요", Toast.LENGTH_LONG).show()
            return
        }
        numOfDays = thisMoim.dates.size

        scheduleData = Array(size = numOfTimes, init = { IntArray( size = numOfDays, init = { 2 } ) } )
    }

    private fun initScheduleTable(v: View) {

        //time 시간표 삽입
        timeLayout = v.findViewById(R.id.personal_schedule_time_linearLayout) as LinearLayout

        val timeTextPm = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1F)

        lateinit var timeText: TextView

        var i: Int = 0
        while( i <= numOfTimes ) {
            timeText = TextView(context)
            timeText.layoutParams = timeTextPm
            timeText.text = String.format("%d시", thisMoim.startTimeHour + i )
            timeText.textSize = 10F
            timeText.setTextColor(
                ContextCompat.getColor(
                    requireActivity().applicationContext,
                    R.color.black
                )
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                timeText.typeface = resources.getFont(R.font.notosans_kr_regular)
            }
            timeText.includeFontPadding = false

            timeLayout.addView(timeText)

            i++
        }

        //date 시간표 삽입
        dateLayout = v.findViewById(R.id.personal_schedule_date_linearLayout) as LinearLayout

        val dateTextPm = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1F)
        dateTextPm.gravity = Gravity.CENTER
        lateinit var dateText: TextView

        i = 0
        while( i < numOfDays ) {
            dateText = TextView(context)
            dateText.layoutParams = dateTextPm
            dateText.text = String.format("%d월\n%d일\n(%c)", thisMoim.dates[i].month, thisMoim.dates[i].day, thisMoim.dates[i].dayOfWeek)

            dateText.setTextColor(
                ContextCompat.getColor(
                    requireActivity().applicationContext,
                    R.color.moim_main_1
                )
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dateText.typeface = resources.getFont(R.font.notosans_kr_regular)
            }
            dateText.includeFontPadding = false
            dateText.gravity = Gravity.CENTER
            dateText.textSize = 10F

            dateLayout.addView(dateText)

            i++
        }

        scheduleLayout = v.findViewById(R.id.personal_schedule_tableLayout) as TableLayout

        timeRows = Array<TableRow>(size = numOfTimes, init = { TableRow(v.context) } ) //한개 행 = 날짜 개수
        scheduleButtons = Array<Array<Button>>(size = numOfTimes,
            init = { Array<Button>(size = numOfDays, init = {Button(v.context)} ) }
        )

        val rowPm: TableLayout.LayoutParams = TableLayout.LayoutParams(0, 40, 1F)
        val cellPm: TableRow.LayoutParams =
            TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1F)

        i = 0
        var j: Int

        while( i < numOfTimes ) {
            //TableRow 생성
            timeRows[i].layoutParams = rowPm
            scheduleLayout.addView(timeRows[i])

            j = 0
            while( j < numOfDays ) {
                //TableRow 안에 Button(cell) 생성
                scheduleButtons[i][j].setBackgroundResource(R.drawable.bg_schedule_cell_btn2)
//                scheduleButtons[i][j].layoutParams = cellPm

                scheduleButtons[i][j].setOnClickListener(CellClickListener(scheduleData, i, j))
                timeRows[i].addView(scheduleButtons[i][j])
                j += 1
            }
            i += 1
        }
    }

    public fun resetScheduleTable() {
        var i: Int = 0
        var j: Int
        while ( i < numOfTimes ) {
            j = 0
            while ( j < numOfDays ) {
                scheduleData[i][j] = 2
                scheduleButtons[i][j].setBackgroundResource(R.drawable.bg_schedule_cell_choice2_possible_btn)
                j++
            }
            i++
        }
    }
}

private open class CellClickListener(protected var scheduleResult: Array<IntArray>, protected var xAxis: Int, protected var yAxis: Int): View.OnClickListener {
    override fun onClick(v: View?) {
        when(selectedBtnFunc) {
            0 -> {
                //default -> 아무것도 선택하지 않았을때, 생략
            }
            1 -> {
                v!!.setBackgroundResource(R.drawable.bg_schedule_cell_choice1_like_btn)
                scheduleResult[xAxis][yAxis] = 1
            }
            2 -> {
                v!!.setBackgroundResource(R.drawable.bg_schedule_cell_choice2_possible_btn)
                scheduleResult[xAxis][yAxis] = 2
            }
            3 -> {
                v!!.setBackgroundResource(R.drawable.bg_schedule_cell_choice3_dislike_btn)
                scheduleResult[xAxis][yAxis] = 3
            }
            4 -> {
                v!!.setBackgroundResource(R.drawable.bg_schedule_cell_choice4_impossible_btn)
                scheduleResult[xAxis][yAxis] = 4
            }
        }
    }
}