package com.legends.moim.src.groupMoim

import android.app.Dialog
import android.content.ClipDescription
import android.content.Intent
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.Window
import com.legends.moim.R
import com.legends.moim.config.BaseActivity
import com.legends.moim.databinding.ActivityMoimGroupBinding
import com.legends.moim.src.main.MainActivity
import com.legends.moim.src.makeMoim.TimeDialog
import com.legends.moim.src.makeMoim.model.makingMoim
import java.util.*

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

        //~ 명 참여 초기화 필요. 인원 파악 데이터 필요. 일단은 더미로 설정.
        val participantDummy = "8"
        binding.moimGroupParticipantTv.text = participantDummy + "명 참여"
        binding.moimGroupHomeBtn.setOnClickListener(this)
        binding.moimGroupInviteBtn.setOnClickListener(this)
        binding.moimGroupParticipantTv.setOnClickListener(this)
        binding.moimGroupAddPersonalBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id) {

            R.id.moim_group_home_btn -> { //홈으로 돌아가기. 모임 저장은 생성 및 적용(개인쪽)시에 구현.
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.moim_group_invite_btn -> { //모임원 초대. 카카오톡 링크로 고민중.
                //todo 그룹 초대 링크 생성 -> 클립보드 붙여넣기
            }
            R.id.moim_group_participant_tv -> { //참가인원 조회, ~명 참여, 누르면 참가자 명단도 나오도록.
                val dig = ParticipantsDialog(this)
                dig.tvMessage.text = "박찬규" + ", " + "박민우"
                dig.showTimeDialog()

            }
            R.id.moim_group_add_personal_btn -> { //개인 시간표 추가
                val intent = Intent(this, MoimPersonalActivity::class.java)
                startActivity(intent)
            }
            //모드 1, 2로 한눈에 보기 시간표 보여주기 fragment
            //인원 상세보기 - 색칠된 시간표의 해당 멤버들 보여주기 기능
        }
    }

    private class CellClickListener(): View.OnClickListener {
        override fun onClick(view: View?) {
            TODO("Not yet implemented")
        }

    }
}