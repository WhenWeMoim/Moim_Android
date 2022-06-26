package com.legends.moim.src.groupMoim

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import com.legends.moim.R
import com.legends.moim.config.baseModel.Moim
import com.legends.moim.databinding.ActivityMoimGroupBinding
import com.legends.moim.src.groupMoim.model.UserSchedules
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

class GroupScheduleFragment(private val activityBinding: ActivityMoimGroupBinding,
                            private val moim: Moim, private val schedule: Array<UserSchedules>?): Fragment() {

    private var numOfDays = moim.dates.size //행 수 = 선택한 날짜 개수
    private var numOfTimes = moim.endTimeHour - moim.startTimeHour //열 수 = 시간 구간 개수

    private lateinit var dateLayout: LinearLayout
    private lateinit var timeLayout: LinearLayout
    private lateinit var scheduleLayout: TableLayout
    private lateinit var timeRows: Array<TableRow>
    private lateinit var scheduleButtons: Array<Array<Button>>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        val v: View = inflater.inflate(R.layout.fragment_schedule_group, container, false)

        getData()

        initScheduleTable(v)

        return v
    }

    private fun getData() {
        val interval: Int = moim.endTimeHour - moim.startTimeHour
        if( interval <= 0 ) {
            Toast.makeText(context, "시간 시간표 생성 중 오류가 발생했습니다. 다시 시도해주세요", Toast.LENGTH_LONG).show()
            return
        }
        numOfTimes = interval

        if( moim.dates.isEmpty() ) {
            Toast.makeText(context, "날짜 시간표 생성 중 오류가 발생했습니다. 다시 시도해주세요", Toast.LENGTH_LONG).show()
            return
        }
        numOfDays = moim.dates.size

    }

    private fun initScheduleTable(v: View) {
        setTimeLayout(v)


        //date 시간표 삽입
        dateLayout = v.findViewById(R.id.group_schedule_date_linearLayout) as LinearLayout

        val dateTextPm = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1F)
            dateTextPm.gravity = Gravity.CENTER
        lateinit var dateText: TextView

        var i = 0
        while( i < numOfDays ) {
            dateText = TextView(context)
            dateText.layoutParams = dateTextPm
//            dateText.text = String.format("%d월\n%d일\n(%c)", moim.dates[i].month, moim.dates[i].day, moim.dates[i].dayOfWeek)
            dateText.text = String.format("${moim.dates[i].month}월\n${moim.dates[i].day}일\n(${moim.dates[i].dayOfWeek})")

            dateText.setTextColor(getColor(requireActivity().applicationContext, R.color.moim_main_1))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dateText.typeface = resources.getFont(R.font.notosans_kr_regular)
            }
            dateText.includeFontPadding = false
            dateText.gravity = Gravity.CENTER
            dateText.textSize = 10F

            dateLayout.addView(dateText)

            i++
        }

        scheduleLayout = v.findViewById(R.id.group_schedule_tableLayout) as TableLayout

        timeRows = Array<TableRow>(size = numOfTimes, init = { TableRow(v.context) } ) //한개 행 = 날짜 개수
        scheduleButtons = Array<Array<Button>>(size = numOfTimes,
            init = { Array<Button>(size = numOfDays, init = {Button(v.context)} ) }
        )

        val rowPm: TableLayout.LayoutParams = TableLayout.LayoutParams(0, 40, 1F)
        val cellPm: TableRow.LayoutParams =
            TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1F)

        i = 0
        var j: Int
        //todo schedules 스케줄데이터 가져오기.

        var scheduleCount: Int = 0

        while ( i < numOfTimes ) {
            //TableRow 생성
            timeRows[i] = TableRow(v.context)
            timeRows[i].layoutParams = rowPm
            scheduleLayout.addView(timeRows[i])

            j = 0
            while ( j < numOfDays ) {
                //TableRow 안에 Button(cell) 생성
                scheduleButtons[i][j].setBackgroundResource(R.drawable.bg_schedule_cell_choice2_possible_btn)
                scheduleButtons[i][j].layoutParams = cellPm

//                cellButtons[i][j]?.setOnClickListener(CellClickListener(scheduleResult, i, j)) //todo 클릭했을 때 인원들 의견 표시
                timeRows[i].addView(scheduleButtons[i][j])
                j += 1
            }
            i += 1
        }

        //여기서 모임 시간표 색깔 삽입.
        if(schedule != null){
            val size = numOfTimes*numOfDays

            val numOfParticipants: Int = schedule.size
            val groupSchedulesArr = Array(size = numOfParticipants, init = { IntArray( size = numOfTimes*numOfDays, init = { 1 } ) } )

            for(i in 0 until numOfParticipants){

                for( j in 0 until size ) {
                    if( schedule[i].schedules != null)
                        groupSchedulesArr[i][j] = ( schedule[i].schedules!! )[j].digitToInt(10)
                }
            }


            j = 0
            while ( j < numOfDays ) {
                i = 0
                while ( i < numOfTimes ) {
                    var sumNumberOfColor = 0
                    var numOfImpossible = 0 //미리 Array<UserSchedules> 조사 해서 불가능 인원 조사.
                    //var intArr = Array(numOfTimes*numOfDays, {1})//임시 UserSchedules배열

                    for(k:Int in 0 until numOfParticipants){
                        //var tempI:Int = groupSchedulesArr[k][numOfDays*j+numOfTimes]
                        if(groupSchedulesArr[k][numOfTimes*j+i] == 1)
                            numOfImpossible += 1
                        sumNumberOfColor += convertScheduleData(groupSchedulesArr[k][numOfTimes*j+i])

                        scheduleButtons[i][j].setOnClickListener( GroupCellClickListener( requireContext(), activityBinding,schedule, i, j, numOfTimes) )

                    }

                    if(numOfImpossible > (numOfParticipants/2)){ //과반수 불가능
                        scheduleButtons[i][j].setBackgroundResource(R.drawable.bg_schedule_cell_impossible_high)
                    }
                    else{
                        moimScheduleColor(scheduleButtons[i][j], sumNumberOfColor, numOfParticipants)
                    }
                    scheduleCount += 1
                    i += 1
                }
                j += 1
            }
        }
    }

    private fun setTimeLayout(v: View) {
        //time 시간표 삽입
        timeLayout = v.findViewById(R.id.group_schedule_time_linearLayout) as LinearLayout

        val timeTextPm = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1F)
        timeTextPm.gravity = Gravity.END

        lateinit var timeText: TextView

        var i = 0

        timeText = TextView(context)
        timeText.text = String.format("")
        timeText.textSize = 44.197F
        timeLayout.addView(timeText)

        while (i <= numOfTimes) {
            timeText = TextView(context)
            timeText.layoutParams = timeTextPm
            timeText.text = String.format("%d시", moim.startTimeHour + i)
            timeText.textSize = 10F
            timeText.setTextColor(getColor(requireActivity().applicationContext, R.color.black))
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

    private fun convertScheduleData(ScheduleData: Int): Int{
        var convertedData: Int = 0
        when(ScheduleData){
            CHOICE_IMPOSSIBLE -> {
                convertedData = -2
            }
            CHOICE_DISLIKE -> {
                convertedData = -1
            }
            CHOICE_POSSIBLE -> {
                convertedData = 1
            }
            CHOICE_LIKE -> {
                convertedData = 2
            }
        }
        return convertedData

    }

    private fun moimScheduleColor(scheduleButtonsView: Button, sumNumberOfColor: Int, numOfUsers: Int){

        if(sumNumberOfColor < 0){
            scheduleButtonsView.setBackgroundResource(R.drawable.bg_schedule_cell_impossible_low)
        }
        else if(sumNumberOfColor >= 0 && sumNumberOfColor < (numOfUsers/2)){
            scheduleButtonsView.setBackgroundResource(R.drawable.bg_schedule_cell_possible_low)
        }
        else if(sumNumberOfColor >= (numOfUsers/2) && sumNumberOfColor < numOfUsers){
            scheduleButtonsView.setBackgroundResource(R.drawable.bg_schedule_cell_possible_middle)
        }
        else if(sumNumberOfColor >= numOfUsers){
            scheduleButtonsView.setBackgroundResource(R.drawable.bg_schedule_cell_possible_high)
        }

    }
}

private open class GroupCellClickListener(private val context: Context,
                                          protected val binding: ActivityMoimGroupBinding,
                                          protected val schedule: Array<UserSchedules>?,
                                          protected val xAxis: Int, protected val yAxis: Int,
                                          private val yMax: Int): View.OnClickListener {
    override fun onClick(v: View?) {

        binding.moimGroupLikeMemberLinearLayout.removeAllViews()
        binding.moimGroupDislikeMemberLinearLayout.removeAllViews()
        binding.moimGroupPossibleMemberLinearLayout.removeAllViews()
        binding.moimGroupImpossibleMemberLinearLayout.removeAllViews()

        val scheduleCellStringIndex = yMax*yAxis + xAxis
        if( schedule != null ) {
            for ( i in schedule.indices ) {
                if( schedule[i].schedules != null ) {
                    when (schedule[i].schedules!![scheduleCellStringIndex].digitToInt(10) ) {
                        CHOICE_LIKE -> addTextInLayout(schedule[i].userName, CHOICE_LIKE)
                        CHOICE_POSSIBLE -> addTextInLayout(schedule[i].userName, CHOICE_POSSIBLE)
                        CHOICE_DISLIKE -> addTextInLayout(schedule[i].userName, CHOICE_DISLIKE)
                        CHOICE_IMPOSSIBLE -> addTextInLayout(schedule[i].userName, CHOICE_IMPOSSIBLE)
                    }
                }
            }
        }
    }
    private fun addTextInLayout(name: String, toWhere: Int) {
        val nameTextPm = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            nameTextPm.gravity = Gravity.CENTER
            nameTextPm.setMargins(2, 2, 2, 2)

        val nameText = TextView(context)
            nameText.layoutParams = nameTextPm
            nameText.text = name

            nameText.setTextColor( ContextCompat.getColor( context, R.color.white ) )
            nameText.includeFontPadding = false
            nameText.gravity = Gravity.CENTER
            nameText.textSize = 14F

        when( toWhere ) {
            CHOICE_LIKE -> binding.moimGroupLikeMemberLinearLayout.addView(nameText)
            CHOICE_POSSIBLE -> binding.moimGroupPossibleMemberLinearLayout.addView(nameText)
            CHOICE_DISLIKE -> binding.moimGroupDislikeMemberLinearLayout.addView(nameText)
            CHOICE_IMPOSSIBLE -> binding.moimGroupImpossibleMemberLinearLayout.addView(nameText)
        }
    }
}