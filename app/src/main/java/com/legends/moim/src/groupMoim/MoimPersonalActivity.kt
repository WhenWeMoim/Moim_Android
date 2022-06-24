package com.legends.moim.src.groupMoim

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.legends.moim.R
import com.legends.moim.config.BaseActivity
import com.legends.moim.databinding.ActivityMoimPersonalBinding
import com.legends.moim.src.groupMoim.model.selectedBtnFunc
import com.legends.moim.src.groupMoim.model.thisMoim
import com.legends.moim.utils.retrofit.RetrofitService
import com.legends.moim.utils.retrofit.ServerView

class MoimPersonalActivity: BaseActivity(), ServerView {

    lateinit var binding: ActivityMoimPersonalBinding

    private lateinit var fragmentManager: FragmentManager
    private lateinit var transaction: FragmentTransaction
    private lateinit var personalScheduleFragment: PersonalScheduleFragment

    private lateinit var choiceButtons: Array<AppCompatButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoimPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setInitialize()
        initView()

        setPersonalScheduleFragment()
    }

    private fun setPersonalScheduleFragment() {
        fragmentManager = supportFragmentManager
        transaction = fragmentManager.beginTransaction()
        personalScheduleFragment = PersonalScheduleFragment(thisMoim)

        transaction
            .replace(R.id.moim_personal_schedule_fragment, personalScheduleFragment)
            .commitAllowingStateLoss()
    }

    private fun setInitialize() {
        choiceButtons = arrayOf(
            findViewById(R.id.moim_personal_like_btn),
            findViewById(R.id.moim_personal_possible_btn),
            findViewById(R.id.moim_personal_dislike_btn),
            findViewById(R.id.moim_personal_impossible_btn)
        )
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
                //todo 서버로 전송
                //todo 그룹 시간표에 적용
                finish()
            }
            R.id.moim_personal_load_btn -> { // User 스케줄 불러오기 -> 아직 구현 x
                binding.moimPersonalLoadBtn.visibility = View.INVISIBLE
            }
            R.id.moim_personal_like_btn -> {
                choiceButtonSelect(1)
            }
            R.id.moim_personal_possible_btn -> {
                choiceButtonSelect(2)
            }
            R.id.moim_personal_dislike_btn -> {
                choiceButtonSelect(3)
            }
            R.id.moim_personal_impossible_btn -> {
                choiceButtonSelect(4)
            }
            R.id.moim_personal_reset_btn -> { //Table 초기화
                personalScheduleFragment.resetScheduleTable()
            }
        }
    }

    private fun choiceButtonSelect(choiceNum: Int) {
        selectedBtnFunc = choiceNum

        for( i in choiceButtons.indices ) {
            if ( i+1 == choiceNum ) {
                choiceButtons[i].isSelected = true
                continue
            }
            choiceButtons[i].isSelected = false
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