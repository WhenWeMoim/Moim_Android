package com.legends.moim.src.groupMoim.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.legends.moim.R

class ScheduleViewGrid(context: Context, attrs: AttributeSet, defStyle: Int): View(context, attrs, defStyle) {

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val timesCount = 24  //todo makeMoim에서 가져온 날짜 수 가져오기      행 수 = 시간 구간 개수
    var dayCount = 14  //todo makeMoim에서 가져온 시간 구간 수 가져오기           열 수 = 선택한 날짜 개수

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    init {
        //paint.color = context.resources.getColor(R.color.divider_grey)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            paint.color = resources.getColor(R.color.divider_grey, context.theme)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val rowHeight = 60 //context.getWeeklyViewItemHeight() //행 높이 임시로 60 설정

        for (i in 0 until timesCount) {
            val y = rowHeight * i.toFloat()
            canvas.drawLine(0f, y, width.toFloat(), y, paint)
        }

        val rowWidth = width / dayCount.toFloat() //

        for (i in 0 until dayCount) {
            val x = rowWidth * i.toFloat()
            canvas.drawLine(x, 0f, x, height.toFloat(), paint)
        }
    }
}