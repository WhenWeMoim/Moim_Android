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
            R.id.moim_group_home_btn -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.moim_group_invite_btn -> {

            }
            R.id.moim_group_participant_tv -> {

            }
            R.id.moim_group_add_personal_btn -> {
                
            }
        }
    }
}