package com.legends.moim.src.groupMoim

import android.os.Bundle
import android.view.View
import com.legends.moim.R
import com.legends.moim.config.BaseActivity
import com.legends.moim.databinding.ActivityMoimPersonalBinding

class MoimPersonalActivity: BaseActivity() {

    lateinit var binding: ActivityMoimPersonalBinding

    private val _personalScheduleFragment: PersonalScheduleFragment
        = supportFragmentManager.findFragmentById(R.id.moim_personal_schedule_fragment) as PersonalScheduleFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoimPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
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
                selectedBtnFunc = 1
            }
            R.id.moim_personal_possible_btn -> {
                selectedBtnFunc = 2
            }
            R.id.moim_personal_dislike_btn -> {
                selectedBtnFunc = 3
            }
            R.id.moim_personal_impossible_btn -> {
                selectedBtnFunc = 4
            }
            R.id.moim_personal_reset_btn -> { //Table 초기화
                _personalScheduleFragment.resetScheduleTable()
            }
        }
    }
}