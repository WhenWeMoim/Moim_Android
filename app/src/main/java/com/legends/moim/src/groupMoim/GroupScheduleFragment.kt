package com.legends.moim.src.groupMoim

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.legends.moim.R
import com.legends.moim.databinding.FragmentScheduleGroupBinding

/**
 * 한 개 행(row) : 한 시간대
 * 한 개 열(column) : 하루
 * 배열은 두개가 들어감. 관리를 위한 실제 버튼 배열(buttons), 스케줄 결과가 들어가는 배열(resultSchedule)
 */

class GroupScheduleFragment: Fragment() {

    private var numOfDays = 7 //임시데이터 -> todo 행 수 = 선택한 날짜 개수 makingMoim.Date.size
    private var numOfTimes = 12 //makingMoim.endTimeHour - makingMoim.startTimeHour //열 수 = 시간 구간 개수

    private lateinit var scheduleLayout: TableLayout

    private lateinit var timeRows: Array<TableRow?>
    private lateinit var scheduleButtons: Array<Array<Button>>

    private lateinit var scheduleData: Array<Array<Int>>

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
    }

    private fun initScheduleTable(v: View) {
        scheduleLayout = v.findViewById(R.id.group_schedule_tableLayout) as TableLayout

        timeRows = arrayOfNulls<TableRow>(numOfTimes) //한개 행 = 날짜 개수
        scheduleButtons = Array<Array<Button>>(size = numOfTimes,
            init = { Array<Button>(size = numOfDays, init = {Button(v.context)} ) }
        )

        val rowPm: TableLayout.LayoutParams = TableLayout.LayoutParams(0, 0, 1F)
        val cellPm: TableRow.LayoutParams =
            TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1F)

        var i: Int = 0
        var j: Int

        while ( i < numOfTimes ) {
            //TableRow 생성
            timeRows[i] = TableRow(v.context)
            timeRows[i]?.layoutParams = rowPm
            scheduleLayout.addView(timeRows[i])

            j = 0
            while ( j < numOfDays ) {
                //TableRow 안에 Button(cell) 생성
//                scheduleButtons[i][j] = Button(v.context)
                scheduleButtons[i][j].setBackgroundResource(R.drawable.bg_schedule_cell_btn)
                scheduleButtons[i][j].layoutParams = cellPm

//                cellButtons[i][j]?.setOnClickListener(CellClickListener(scheduleResult, i, j))
                timeRows[i]?.addView(scheduleButtons[i][j])
                j += 1
            }
            i += 1
        }
    }
}