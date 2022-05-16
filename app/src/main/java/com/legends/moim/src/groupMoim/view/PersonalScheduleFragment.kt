package com.legends.moim.src.groupMoim.view

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

    lateinit var binding: FragmentSchedulePersonalBinding

    private val numOfDays = 7 //임시데이터 -> todo 행 수 = 선택한 날짜 개수 makingMoim.Date.size
    private val numOfTimes = 12 //임시데이터 -> todo makingMoim.endTimeHour - makingMoim.startTimeHour //열 수 = 시간 구간 개수

    private lateinit var scheduleLayout: TableLayout

    private lateinit var dayRows: Array<TableRow?>
    private lateinit var cellButtons: Array<Array<Button?>>
    private lateinit var scheduleResult: Array<IntArray>

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSchedulePersonalBinding.inflate(layoutInflater)
        val v: View = inflater.inflate(R.layout.fragment_schedule_group, container, false)

        initView(v)

        initScheduleTable(v)

        return v
    }

    private fun initScheduleTable(v: View) {
        val rowPm = TableLayout.LayoutParams(0, 0, 1F)
        val cellPm: TableLayout.LayoutParams =
            TableLayout.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1F)

        var i: Int = 0
        var j: Int = 0

        i = 0
        while (i < numOfDays) {
            //TableRow 생성
            dayRows[i] = TableRow(v.context)
            dayRows[i]?.layoutParams = rowPm
            scheduleLayout!!.addView(dayRows[i])

            j = 0
            while (j < numOfTimes) {
                //TableRow 안에 Button(cell) 생성
                cellButtons[i][j] = Button(v.context)
                cellButtons[i][j]?.setBackgroundResource(R.drawable.bg_schedule_cell_btn)
                cellButtons[i][j]?.layoutParams = cellPm

                val finalI = i
                val finalJ = j
                cellButtons[i][j]?.setOnClickListener(object : PixelClickListener(finalI, finalJ) {
                    override fun onClick(v: View) {
                        val bundle = arguments //번들(선택한 선호도) 안의 텍스트 불러오기
                        if (bundle != null) {
                            val color = v.resources.getColor(bundle.getInt("color"))
                            val choice = bundle.getInt("choice")
                            Log.d("RECV_COLOR_FROM_ACT", Integer.toString(color))
                            cellButtons[finalI][finalJ]?.setBackgroundColor(color)
                            scheduleResult[finalI][finalJ] = choice
                        } else {
                            //pixels[finalI][finalJ]?.setBackgroundColor(R.color.orange700)
                        }
                    }
                })
                dayRows[i]?.addView(cellButtons[i][j])
                j += 1
            }
            i += 1
        }
    }

    private fun initView(v: View) {
        scheduleLayout = v.findViewById(R.id.personal_schedule_tableLayout) as TableLayout

        dayRows = arrayOfNulls<TableRow>(numOfTimes) //한개 행 = 하루
        cellButtons = Array<Array<Button?>>(numOfDays) {
            arrayOfNulls<Button>(numOfTimes)
        }

        scheduleResult =
            Array(size = numOfDays, init = { IntArray(size = numOfTimes, init = { 2 }) })
    }
}

private abstract class PixelClickListener(protected var xAxis: Int, protected var yAxis: Int) : View.OnClickListener

private class CellClickListener(protected var xAxis: Int, protected var yAxis: Int): View.OnClickListener {
    override fun onClick(view: View?) {
        TODO("Not yet implemented")
    }
}