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
 * 한 개 행(row) : 한 시간대
 * 한 개 열(column) : 하루
 * 배열은 두개가 들어감. 관리를 위한 실제 버튼 배열(buttons), 스케줄 결과가 들어가는 배열(resultSchedule)
 */

class PersonalScheduleFragment: Fragment() {

    private lateinit var binding: FragmentSchedulePersonalBinding

    private var numOfDays = 7 //임시데이터 -> todo 행 수 = 선택한 날짜 개수 makingMoim.Date.size
    private val numOfTimes = 12 //임시데이터 -> todo makingMoim.endTimeHour - makingMoim.startTimeHour //열 수 = 시간 구간 개수

    private lateinit var scheduleLayout: TableLayout

    private lateinit var timeRows: Array<TableRow> //한개 행(row)
    private lateinit var scheduleButtons: Array<Array<Button>>

    private var scheduleResult= Array(size = numOfDays, init = { IntArray(size = numOfTimes, init = { 2 } ) } )

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSchedulePersonalBinding.inflate(layoutInflater)
        val v: View = inflater.inflate(R.layout.fragment_schedule_personal, container, false)

        getData()

        initScheduleTable(v)

        return v
    }

    private fun getData() {

    }

    private fun initScheduleTable(v: View) {
        scheduleLayout = v.findViewById(R.id.personal_schedule_tableLayout) as TableLayout

        timeRows = Array<TableRow>(size = numOfTimes, init = { TableRow(v.context) } ) //한개 행 = 날짜 개수
        scheduleButtons = Array<Array<Button>>(size = numOfTimes,
            init = { Array<Button>(size = numOfDays, init = {Button(v.context)} ) }
        )

        val rowPm: TableLayout.LayoutParams = TableLayout.LayoutParams(0, 0, 1F)
        val cellPm: TableRow.LayoutParams =
            TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1F)

        var i: Int = 0
        var j: Int

        while( i < numOfTimes ) {
            //TableRow 생성
            timeRows[i].layoutParams = rowPm
            scheduleLayout.addView(timeRows[i])

            j = 0
            while( j < numOfDays ) {
                //TableRow 안에 Button(cell) 생성
                scheduleButtons[i][j].setBackgroundResource(R.drawable.bg_schedule_cell_btn)
                scheduleButtons[i][j].layoutParams = cellPm

                scheduleButtons[i][j].setOnClickListener(CellClickListener(scheduleResult, i, j))
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
                scheduleResult[i][j] = 2
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