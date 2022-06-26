package com.legends.moim.src.groupMoim

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.legends.moim.R
import com.legends.moim.config.baseModel.Moim
import com.legends.moim.src.groupMoim.model.selectedBtnFunc
import com.legends.moim.utils.CHOICE_DISLIKE
import com.legends.moim.utils.CHOICE_IMPOSSIBLE
import com.legends.moim.utils.CHOICE_LIKE
import com.legends.moim.utils.CHOICE_POSSIBLE

/**
 * 한 개 행(row) : 한 시간대
 * 한 개 열(column) : 하루
 * 배열은 두개가 들어감. 관리를 위한 실제 버튼 배열(buttons), 스케줄 결과가 들어가는 배열(resultSchedule)
 */

class PersonalScheduleFragment(private val moim: Moim, private val schedule: Array<IntArray>?): Fragment() {

    private var numOfDays = moim.dates.size //행 수 = 선택한 날짜 개수
    private var numOfTimes = moim.endTimeHour - moim.startTimeHour //열 수 = 시간 구간 개수

    private lateinit var dateLayout: LinearLayout
    private lateinit var timeLayout: LinearLayout
    private lateinit var scheduleLayout: TableLayout

    private lateinit var timeRows: Array<TableRow> //한개 행(row)
    private lateinit var scheduleButtons: Array<Array<Button>>

    private var scheduleData = schedule

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val v: View = inflater.inflate(R.layout.fragment_schedule_personal, container, false)

        getMoimData()

        initScheduleTable(v)

        //todo 이미 데이터가 있는 경우, 스케줄표에 적용해야 함

        return v
    }

    private fun getMoimData() {
        //val numOfTimes: Int = moim.endTimeHour - moim.startTimeHour
        if( numOfTimes <= 0 ) {
            Toast.makeText(context, "시간 시간표 생성 중 오류가 발생했습니다. 다시 시도해주세요", Toast.LENGTH_LONG).show()
            return
        }

        if( moim.dates.isEmpty() ) {
            Toast.makeText(context, "날짜 시간표 생성 중 오류가 발생했습니다. 다시 시도해주세요", Toast.LENGTH_LONG).show()
            return
        }

        if(scheduleData == null)
            scheduleData = Array(size = numOfTimes, init = { IntArray( size = numOfDays, init = { CHOICE_POSSIBLE } ) } )
        //numOfDays = moim.dates.size
    }

    private fun initScheduleTable(v: View) {
        //time 시간표 삽입
        setTimeLayout(v)

        //date 시간표 삽입
        setDateLayout(v)

        //시간표 Cell 삽입
        setScheduleLayout(v)
    }

    private fun setScheduleLayout(v: View) {
        scheduleLayout = v.findViewById(R.id.personal_schedule_tableLayout) as TableLayout

        timeRows = Array<TableRow>(size = numOfTimes, init = { TableRow(v.context) }) //한개 행 = 날짜 개수
        scheduleButtons = Array<Array<Button>>(size = numOfTimes,
            init = { Array<Button>(size = numOfDays, init = { Button(v.context) }) }
        )

        val rowPm: TableLayout.LayoutParams = TableLayout.LayoutParams(0, 40, 1F)
        val cellPm: TableRow.LayoutParams =
            TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1F)

        if (schedule == null) {
            for ( i in 0 until numOfTimes) {
                //TableRow 생성
                timeRows[i].layoutParams = rowPm
                scheduleLayout.addView(timeRows[i])

                for ( j in 0 until numOfDays) {
                    //TableRow 안에 Button(cell) 생성
                    scheduleButtons[i][j].setBackgroundResource(R.drawable.bg_schedule_cell_choice2_possible_btn)
                    scheduleButtons[i][j].layoutParams = cellPm

                    scheduleButtons[i][j].setOnClickListener( PersonalCellClickListener( scheduleData!!, i, j ) )
                    timeRows[i].addView(scheduleButtons[i][j])
                }
            }
        } else {
            for ( i in 0 until numOfTimes) {
                //TableRow 생성
                timeRows[i].layoutParams = rowPm
                scheduleLayout.addView(timeRows[i])

                for ( j in 0 until numOfDays ) {
                    //TableRow 안에 Button(cell) 생성
                    when (scheduleData!![i][j]) {
                        CHOICE_LIKE ->
                            scheduleButtons[i][j].setBackgroundResource(R.drawable.bg_schedule_cell_choice1_like_btn)
                        CHOICE_POSSIBLE ->
                            scheduleButtons[i][j].setBackgroundResource(R.drawable.bg_schedule_cell_choice2_possible_btn)
                        CHOICE_DISLIKE ->
                            scheduleButtons[i][j].setBackgroundResource(R.drawable.bg_schedule_cell_choice3_dislike_btn)
                        CHOICE_IMPOSSIBLE ->
                            scheduleButtons[i][j].setBackgroundResource(R.drawable.bg_schedule_cell_choice4_impossible_btn)
                    }
                    scheduleButtons[i][j].layoutParams = cellPm

                    scheduleButtons[i][j].setOnClickListener( PersonalCellClickListener( scheduleData!!, i, j ) )
                    timeRows[i].addView(scheduleButtons[i][j])
                }
            }
        }
    }

    private fun setDateLayout(v: View) {
        dateLayout = v.findViewById(R.id.personal_schedule_date_linearLayout) as LinearLayout

        val dateTextPm = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1F)
            dateTextPm.gravity = Gravity.CENTER
        lateinit var dateText: TextView

        var i = 0
        while (i < numOfDays) {
            dateText = TextView(context)
            dateText.layoutParams = dateTextPm
    //            dateText.text = String.format("%d월\n%d일\n(%c)", moim.dates[i].month, moim.dates[i].day, moim.dates[i].dayOfWeek)
            dateText.text =
                String.format("${moim.dates[i].month}월\n${moim.dates[i].day}일\n(${moim.dates[i].dayOfWeek})")

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
    }

    private fun setTimeLayout(v: View) {
        timeLayout = v.findViewById(R.id.personal_schedule_time_linearLayout) as LinearLayout

        val timeTextPm = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1F)
        timeTextPm.gravity = Gravity.RIGHT
        lateinit var timeText: TextView

        var i: Int = 0

        timeText = TextView(context)
        timeText.text = String.format("")
        timeText.textSize = 44.197F
        timeLayout.addView(timeText)

        while (i <= numOfTimes) {
            timeText = TextView(context)
            timeText.layoutParams = timeTextPm
            timeText.text = String.format("%d시", moim.startTimeHour + i)
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
            timeText = TextView(context)
            timeText.text = String.format("")
            timeText.textSize = 24.619F
            timeLayout.addView(timeText)
            i++
        }
    }

    public fun resetScheduleTable() {
        for ( i in 0 until numOfTimes ) {
            for ( j in 0 until numOfDays ) {
                scheduleData!![i][j] = CHOICE_POSSIBLE
                scheduleButtons[i][j].setBackgroundResource(R.drawable.bg_schedule_cell_choice2_possible_btn)
            }
        }
    }

    public fun getScheduleData(): Array<IntArray> = scheduleData!!
    public fun getNumOfDays(): Int = numOfDays
    public fun getNumOfTimes(): Int = numOfTimes
}

private open class PersonalCellClickListener(protected var scheduleResult: Array<IntArray>, protected var xAxis: Int, protected var yAxis: Int): View.OnClickListener {
    override fun onClick(v: View?) {
        when(selectedBtnFunc) {
            CHOICE_LIKE -> {
                v!!.setBackgroundResource(R.drawable.bg_schedule_cell_choice1_like_btn)
                scheduleResult[xAxis][yAxis] = CHOICE_LIKE
            }
            CHOICE_POSSIBLE -> {
                v!!.setBackgroundResource(R.drawable.bg_schedule_cell_choice2_possible_btn)
                scheduleResult[xAxis][yAxis] = CHOICE_POSSIBLE
            }
            CHOICE_DISLIKE -> {
                v!!.setBackgroundResource(R.drawable.bg_schedule_cell_choice3_dislike_btn)
                scheduleResult[xAxis][yAxis] = CHOICE_DISLIKE
            }
            CHOICE_IMPOSSIBLE -> {
                v!!.setBackgroundResource(R.drawable.bg_schedule_cell_choice4_impossible_btn)
                scheduleResult[xAxis][yAxis] = CHOICE_IMPOSSIBLE
            }
            else -> {
                //error
            }
        }
    }
}