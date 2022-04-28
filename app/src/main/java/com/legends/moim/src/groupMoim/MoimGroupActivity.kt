package com.legends.moim.src.groupMoim

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.legends.moim.R
import com.legends.moim.config.BaseActivity
import com.legends.moim.databinding.ActivityMoimGroupBinding
import com.legends.moim.src.main.MainActivity

class MoimGroupActivity : BaseActivity() {

    lateinit var binding : ActivityMoimGroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoimGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    fun initView() {
        binding.moimGroupMoimNameTv.text = "__그룹이름"
        binding.moimGroupMoimExplainTv.text = "__모임 설명"

        binding.moimGroupHomeBtn.setOnClickListener(this)
        binding.moimGroupInviteBtn.setOnClickListener(this)
        binding.moimGroupParticipantTv.setOnClickListener(this)
        binding.moimGroupAddPersonalBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id) {
            R.id.main_view_moim_btn -> {
                //모임 조회
            }
            R.id.main_user_btn -> {
                //유저
            }
            R.id.moim_group_home_btn -> { // 홈으로 돌아가기 기능. 모임 저장은 생성 및 적용(개인쪽)시에 구현.
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.moim_group_invite_btn -> {// 멤버들에게 초대하는 기능. 카카오톡 링크로 고민중.

            }
            R.id.moim_group_participant_tv -> {// 참가자 명단. ~명 참여, 버튼 누르면 참가자 명단도 나오도록.

            }
            R.id.moim_group_add_personal_btn -> {//개인 시간표 적용 시키기.
                
            }
            //모드 1, 2로 한눈에 보기 시간표 보여주기 fragment
            //인원 상세보기 - 색칠된 시간표의 해당 멤버들 보여주기 기능
        }
    }
}