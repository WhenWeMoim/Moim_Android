package com.legends.moim.src.groupMoim

import android.R.color
import android.graphics.Color.red
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


class GroupScheduleFragment: Fragment() {

    lateinit var binding: FragmentScheduleGroupBinding

    private val numOfDays = 8 //열 수 = 선택한 날짜 개수
    private val numOfInterval = makingMoim.endTimeHour - makingMoim.startTimeHour //행 수 = 시간 구간 개수

    var scheduleCanvus: TableLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentScheduleGroupBinding.inflate(layoutInflater)

        val v: View = inflater.inflate(R.layout.fragment_schedule_group, container, false)

        scheduleCanvus = v.findViewById(R.id.group_schedule_tableLayout) as TableLayout

        val rows: Array<TableRow?> = arrayOfNulls<TableRow>(numOfInterval)
        val pixels: Array<Array<Button?>> = Array<Array<Button?>>(numOfInterval) {
            arrayOfNulls<Button>( numOfInterval )
        }

        val pm = TableLayout.LayoutParams(0, 0, 1F)
        val rowPm: TableLayout.LayoutParams =
            TableLayout.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1F)

        var i: Int
        var j: Int
        {
            i = 0
            while (i < numOfDays) {
                rows[i] = TableRow(v.getContext())
                rows[i]?.setLayoutParams(pm)
                scheduleCanvus!!.addView(rows[i])
                j = 0
                while ( j < numOfInterval ) {
                    pixels[i][j] = Button(v.getContext())
                    pixels[i][j]?.setBackgroundResource(R.drawable.bg_schedule_cell_btn)
                    pixels[i][j]?.setLayoutParams(rowPm)
                    val finalI = i
                    val finalJ = j
                    pixels[i][j]?.setOnClickListener(object : PixelClickListener(finalI, finalJ) {
                        override fun onClick(v: View) {
                            val bundle = arguments //번들 안의 텍스트 불러오기
                            //assert bundle != null;
                            if (bundle != null) {
                                val color = v.resources.getColor(bundle.getInt("color"))
                                Log.d("RECV_COLOR_FROM_ACT", Integer.toString(color))
                                pixels[finalI][finalJ]?.setBackgroundColor(color)
                            } else {
                                //pixels[finalI][finalJ]?.setBackgroundColor(R.color.orange700)
                            }
                        }
                    })
                    rows[i]?.addView(pixels[i][j])
                    j += 1
                }
                i += 1
            }
        }

        return v
    }
}

private abstract class PixelClickListener(protected var xAxis: Int, protected var yAxis: Int) :
    View.OnClickListener