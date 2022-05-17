package com.legends.moim.src.groupMoim

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import androidx.fragment.app.Fragment
import com.legends.moim.R
import com.legends.moim.databinding.FragmentSchedulePersonalBinding

/**
 * 한개 row = 한개 날짜
 * 한개 column = 한 날짜의 한 시간대
 * 시간표에는 전치(T)된 상태로 보여지게 됨.
 * 즉, 하나의 날짜가 하나의 열로 보이지만, 하나의 행이 하나의 날짜를 표현함
 * 배열은 두개가 들어감. 관리를 위한 실제 버튼 배열(buttons), 스케줄 결과가 들어가는 배열(resultSchedule)
 */
class PersonalScheduleFragment: Fragment() {

    private lateinit var binding: FragmentSchedulePersonalBinding

    private val numOfDays = 7 //임시데이터 -> todo 행 수 = 선택한 날짜 개수 makingMoim.Date.size
    private val numOfTimes = 12 //임시데이터 -> todo makingMoim.endTimeHour - makingMoim.startTimeHour //열 수 = 시간 구간 개수

    private lateinit var scheduleLayout: TableLayout

    private lateinit var dayRows: Array<TableRow?>
    private var cellButtons: Array<Array<Button?>> = Array<Array<Button?>>(numOfDays) {
        arrayOfNulls<Button>(numOfTimes)
    }
    private var scheduleResult= Array(size = numOfDays, init = { IntArray(size = numOfTimes, init = { 2 } ) } )

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSchedulePersonalBinding.inflate(layoutInflater)
        val v: View = inflater.inflate(R.layout.fragment_schedule_personal, container, false)

        initView(v)

        initScheduleTable(v)

        return v
    }

    private fun initView(v: View) {
        scheduleLayout = v.findViewById(R.id.personal_schedule_tableLayout) as TableLayout

        dayRows = arrayOfNulls<TableRow>(numOfTimes) //한개 행 = 하루
    }

    private fun initScheduleTable(v: View) {
        val rowPm: TableLayout.LayoutParams = TableLayout.LayoutParams(0, 0, 1F)
        val cellPm: TableRow.LayoutParams =
            TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1F)

        var i: Int = 0
        var j: Int

        while( i < numOfDays ) {
            //TableRow 생성
            dayRows[i] = TableRow(v.context)
            dayRows[i]?.layoutParams = rowPm
            scheduleLayout.addView(dayRows[i])

            j = 0
            while( j < numOfTimes ) {
                //TableRow 안에 Button(cell) 생성
                cellButtons[i][j] = Button(v.context)
                cellButtons[i][j]?.setBackgroundResource(R.drawable.bg_schedule_cell_btn)
                cellButtons[i][j]?.layoutParams = cellPm

                cellButtons[i][j]?.setOnClickListener(CellClickListener(scheduleResult, i, j))
                dayRows[i]?.addView(cellButtons[i][j])
                j += 1
            }
            i += 1
        }
    }

    public fun resetScheduleTable() {
        var i: Int = 0
        Log.d("AAAAAAAAAA", "$i")
        var j: Int
        while ( i < numOfDays ) {
            j = 0
            while ( j < numOfTimes ) {
                Log.d("BBBBBBBBBB", "$i, $j")
                scheduleResult[i][j] = 2
                cellButtons[i][j]!!.setBackgroundResource(R.drawable.bg_schedule_cell_choice2_possible_btn)
                Log.d("CCCCCCCCCC", "$i, $j")
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