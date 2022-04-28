package com.legends.moim.src.groupMoim

import android.R.color
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


class GroupScheduleFragment: Fragment() {

    lateinit var binding: FragmentScheduleGroupBinding

    private val pixelNum = 8 //픽셀의 개수

    var canvusLayout: TableLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentScheduleGroupBinding.inflate(layoutInflater)

        val v: View = inflater.inflate(R.layout.fragment_schedule_group, container, false)

        canvusLayout = v.findViewById(R.id.group_schedule_tableLayout) as TableLayout

        val rows: Array<TableRow?> = arrayOfNulls<TableRow>(pixelNum)
        val pixels: Array<Array<Button?>> = Array<Array<Button?>>(pixelNum) {
            arrayOfNulls<Button>(
                pixelNum
            )
        }

        val pm = TableLayout.LayoutParams(0, 0, 1)
        val rowPm: TableRow.LayoutParams =
            TableLayout.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1)

        var i: Int
        var j: Int
        {
            i = 0
            while (i < pixelNum) {
                rows[i] = TableRow(v.getContext())
                rows[i].setLayoutParams(pm)
                canvusLayout.addView(rows[i])
                j = 0
                while (j < pixelNum) {
                    pixels[i][j] = Button(v.getContext())
                    pixels[i][j].setBackgroundResource(R.drawable.custom_invisible_btn)
                    pixels[i][j].setLayoutParams(rowPm)
                    val finalI = i
                    val finalJ = j
                    pixels[i][j].setOnClickListener(object : PixelClickListener(finalI, finalJ) {
                        override fun onClick(v: View) {
                            val bundle = arguments //번들 안의 텍스트 불러오기
                            //assert bundle != null;
                            if (bundle != null) {
                                val color = v.resources.getColor(bundle.getInt("color"))
                                Log.d("RECV_COLOR_FROM_ACT", Integer.toString(color))
                                pixels[finalI][finalJ].setBackgroundColor(color)
                            } else {
                                pixels[finalI][finalJ].setBackgroundColor(color)
                            }
                        }
                    })
                    rows[i].addView(pixels[i][j])
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