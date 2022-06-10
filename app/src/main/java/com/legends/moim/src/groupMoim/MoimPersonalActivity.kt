package com.legends.moim.src.groupMoim

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.legends.moim.R
import com.legends.moim.config.BaseActivity
import com.legends.moim.databinding.ActivityMoimPersonalBinding

class MoimPersonalActivity: BaseActivity() {

    lateinit var binding: ActivityMoimPersonalBinding

    private lateinit var fragmentManager: FragmentManager
    private lateinit var transaction: FragmentTransaction
    private lateinit var personalScheduleFragment: PersonalScheduleFragment

    private lateinit var choiceButtons: Array<AppCompatButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setInitialize()
        initView()
    }

    private fun setInitialize() {

        binding = ActivityMoimPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragmentManager = supportFragmentManager
        transaction = fragmentManager.beginTransaction()
        personalScheduleFragment = PersonalScheduleFragment()

        transaction
            .replace(R.id.moim_personal_schedule_fragment, personalScheduleFragment)
            .commitAllowingStateLoss()

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
            if ( i+1 == choiceNum ){
                choiceButtons[i].isPressed = true
                choiceButtons[i].isSelected = true
                break
            }

            choiceButtons[i].isPressed = false
            choiceButtons[i].isSelected = false
        }
    }
}