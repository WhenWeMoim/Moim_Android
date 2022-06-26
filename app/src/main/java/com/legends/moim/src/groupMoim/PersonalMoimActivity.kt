package com.legends.moim.src.groupMoim

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.legends.moim.R
import com.legends.moim.config.BaseActivity
import com.legends.moim.databinding.ActivityMoimPersonalBinding
import com.legends.moim.src.groupMoim.model.mySchedule
import com.legends.moim.src.groupMoim.model.selectedBtnFunc
import com.legends.moim.src.groupMoim.model.thisMoim
import com.legends.moim.utils.*
import com.legends.moim.utils.retrofit.RetrofitService
import com.legends.moim.utils.retrofit.ServerView

class PersonalMoimActivity: BaseActivity(), ServerView {

    lateinit var binding: ActivityMoimPersonalBinding

    private lateinit var fragmentManager: FragmentManager
    private lateinit var transaction: FragmentTransaction
    private lateinit var personalScheduleFragment: PersonalScheduleFragment
    private val choiceButtons = HashMap<Int, View>(4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoimPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setInitialize()
        initView()
    }

    private fun setInitialize() {

        choiceButtons[CHOICE_LIKE] = findViewById(R.id.moim_personal_like_btn)
        choiceButtons[CHOICE_POSSIBLE] = findViewById(R.id.moim_personal_possible_btn)
        choiceButtons[CHOICE_DISLIKE] = findViewById(R.id.moim_personal_dislike_btn)
        choiceButtons[CHOICE_IMPOSSIBLE] = findViewById(R.id.moim_personal_impossible_btn)

        fragmentManager = supportFragmentManager
        transaction = fragmentManager.beginTransaction()

        if( !(intent.getStringExtra("mySchedule").isNullOrEmpty()) ) {
            val intScheduleArray = scheduleString2IntArray(intent.getStringExtra("mySchedule")!!, thisMoim.endTimeHour - thisMoim.startTimeHour, thisMoim.dates.size)
            personalScheduleFragment = PersonalScheduleFragment(thisMoim, intScheduleArray)
        } else
            personalScheduleFragment = PersonalScheduleFragment(thisMoim, null)
        transaction
            .replace(R.id.moim_personal_schedule_fragment, personalScheduleFragment)
            .commitAllowingStateLoss()
    }

    private fun initView() {
        binding.moimPersonalCompleteBtn.setOnClickListener(this)
        binding.moimPersonalLoadBtn.setOnClickListener(this)
        binding.moimPersonalLikeBtn.setOnClickListener(this)
        binding.moimPersonalPossibleBtn.setOnClickListener(this)
        binding.moimPersonalDislikeBtn.setOnClickListener(this)
        binding.moimPersonalImpossibleBtn.setOnClickListener(this)
        binding.moimPersonalResetBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id) {
            R.id.moim_personal_complete_btn -> {
                postPersonalSchedule(personalScheduleFragment.getScheduleData())
            }
            R.id.moim_personal_load_btn -> { // User 스케줄 불러오기 -> 아직 구현 x
                binding.moimPersonalLoadBtn.visibility = View.INVISIBLE
            }
            R.id.moim_personal_like_btn -> {
                choiceButtonSelect(CHOICE_LIKE)
            }
            R.id.moim_personal_possible_btn -> {
                choiceButtonSelect(CHOICE_POSSIBLE)
            }
            R.id.moim_personal_dislike_btn -> {
                choiceButtonSelect(CHOICE_DISLIKE)
            }
            R.id.moim_personal_impossible_btn -> {
                choiceButtonSelect(CHOICE_IMPOSSIBLE)
            }
            R.id.moim_personal_reset_btn -> { //Table 초기화
                personalScheduleFragment.resetScheduleTable()
            }
        }
    }

    private fun choiceButtonSelect(choice: Int) {
        selectedBtnFunc = choice

        choiceButtons[CHOICE_LIKE]!!.isSelected = false
        choiceButtons[CHOICE_DISLIKE]!!.isSelected = false
        choiceButtons[CHOICE_POSSIBLE]!!.isSelected = false
        choiceButtons[CHOICE_IMPOSSIBLE]!!.isSelected = false

        when( choice ) {
            CHOICE_LIKE -> choiceButtons[CHOICE_LIKE]!!.isSelected = true
            CHOICE_DISLIKE -> choiceButtons[CHOICE_DISLIKE]!!.isSelected = true
            CHOICE_POSSIBLE -> choiceButtons[CHOICE_POSSIBLE]!!.isSelected = true
            CHOICE_IMPOSSIBLE -> choiceButtons[CHOICE_IMPOSSIBLE]!!.isSelected = true
        }
    }

    private fun postPersonalSchedule(scheduleData: Array<IntArray>) {
        val numOfDays = personalScheduleFragment.getNumOfDays()
        val numOfTimes = personalScheduleFragment.getNumOfTimes()

        var resultString = ""
        for(i in 0 until numOfDays) {
            for( j in 0 until numOfTimes) {
                resultString += scheduleData[j][i].toString()
            }
        }
        Log.d("postPersonalSchedule", "resultString : $resultString")

        mySchedule = resultString

        val retrofitService = RetrofitService()
        retrofitService.setServerView(this)
        retrofitService.postPersonalSchedule( thisMoim.moimIdx, resultString )
    }

    override fun onServerLoading() {
        // todo Loding Effect
    }

    override fun onServerSuccess() {
        Toast.makeText(this, "개인 스케줄 저장을 완료하였습니다.", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onServerFailure(code: Int, message: String) {
        Toast.makeText(this, "$code Error\n에러가 발생했습니다. $message", Toast.LENGTH_SHORT).show()
    }
}