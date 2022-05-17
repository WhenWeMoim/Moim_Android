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
import com.legends.moim.databinding.FragmentScheduleGroupBinding
import com.legends.moim.src.makeMoim.model.makingMoim

/**
 * 한개 row = 한개 날짜
 * 한개 column = 한 날짜의 한 시간대
 * 시간표에는 전치(T)된 상태로 보여지게 됨.
 * 즉, 하나의 날짜가 하나의 열로 보이지만, 하나의 행이 하나의 날짜를 표현함
 * 배열은 두개가 들어감. 관리를 위한 실제 버튼 배열(buttons), 스케줄 결과가 들어가는 배열(resultSchedule)
 */
class GroupScheduleFragment: Fragment() {

    lateinit var binding: FragmentScheduleGroupBinding

    private val numOfDays = 7 //임시데이터 -> todo 행 수 = 선택한 날짜 개수 makingMoim.Date.size
    private val numOfTimes = 12 //makingMoim.endTimeHour - makingMoim.startTimeHour //열 수 = 시간 구간 개수

    private lateinit var scheduleLayout: TableLayout

    private lateinit var dayRows: Array<TableRow?>
    private lateinit var cellButtons: Array<Array<Button?>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentScheduleGroupBinding.inflate(layoutInflater)
        val v: View = inflater.inflate(R.layout.fragment_schedule_group, container, false)

        initView(v)

        initScheduleTable(v)

        return v
    }

    private fun initView(v: View) {
        scheduleLayout = v.findViewById(R.id.group_schedule_tableLayout) as TableLayout

        dayRows = arrayOfNulls<TableRow>(numOfTimes) //한개 행 = 하루
        cellButtons = Array<Array<Button?>>(numOfDays) {
            arrayOfNulls<Button>(numOfTimes)
        }
    }

    private fun initScheduleTable(v: View) {
        val rowPm: TableLayout.LayoutParams = TableLayout.LayoutParams(0, 0, 1F)
        val cellPm: TableRow.LayoutParams =
            TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1F)

        var i: Int = 0
        var j: Int

        while ( i < numOfDays ) {
            //TableRow 생성
            dayRows[i] = TableRow(v.context)
            dayRows[i]?.layoutParams = rowPm
            scheduleLayout.addView(dayRows[i])

            j = 0
            while ( j < numOfTimes ) {
                //TableRow 안에 Button(cell) 생성
                cellButtons[i][j] = Button(v.context)
                cellButtons[i][j]?.setBackgroundResource(R.drawable.bg_schedule_cell_btn)
                cellButtons[i][j]?.layoutParams = cellPm

//                cellButtons[i][j]?.setOnClickListener(CellClickListener(scheduleResult, i, j))
                dayRows[i]?.addView(cellButtons[i][j])
                j += 1
            }
            i += 1
        }
    }
}