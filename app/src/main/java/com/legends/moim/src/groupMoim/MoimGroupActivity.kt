package com.legends.moim.src.groupMoim

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.legends.moim.R
import com.legends.moim.config.BaseActivity
import com.legends.moim.databinding.ActivityMoimGroupBinding
import com.legends.moim.src.main.MainActivity
import com.legends.moim.src.makeMoim.model.makingMoim

class MoimGroupActivity : BaseActivity() {

    lateinit var binding : ActivityMoimGroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoimGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    fun initView() {
        binding.moimGroupMoimNameTv.text = makingMoim.title
        binding.moimGroupMoimExplainTv.text = makingMoim.description

        binding.moimGroupHomeBtn.setOnClickListener(this)
        binding.moimGroupInviteBtn.setOnClickListener(this)
        binding.moimGroupParticipantTv.setOnClickListener(this)
        binding.moimGroupAddPersonalBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id) {
            R.id.moim_group_home_btn -> { //홈으로 돌아가기
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.moim_group_invite_btn -> { //모임원 초대
                //todo 그룹 초대 링크 생성 -> 클립보드 붙여넣기
            }
            R.id.moim_group_participant_tv -> { //참가인원 조회

            }
            R.id.moim_group_add_personal_btn -> { //개인 시간표 추가
                val intent = Intent(this, MoimPersonalActivity::class.java)
                startActivity(intent)
            }
        }
    }
}