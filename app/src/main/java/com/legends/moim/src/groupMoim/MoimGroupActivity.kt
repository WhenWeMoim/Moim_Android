package com.legends.moim.src.groupMoim

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.gson.Gson
import com.legends.moim.R
import com.legends.moim.config.BaseActivity
import com.legends.moim.config.baseModel.Moim
import com.legends.moim.databinding.ActivityMoimGroupBinding
import com.legends.moim.src.groupMoim.model.GroupScheduleRes
import com.legends.moim.src.groupMoim.model.thisMoim
import com.legends.moim.src.main.JoinMoimDialog
import com.legends.moim.src.main.MainActivity
import com.legends.moim.utils.FLAG_ACTIVITY_MAIN
import com.legends.moim.utils.FLAG_ACTIVITY_MAKEMOIM
import com.legends.moim.utils.FLAG_ACTIVITY_VIEWMOIM
import com.legends.moim.utils.retrofit.GetMoimView
import com.legends.moim.utils.retrofit.RetrofitService


class MoimGroupActivity : BaseActivity(), GetMoimView {

    private lateinit var binding : ActivityMoimGroupBinding
    private lateinit var groupScheduleFragment: GroupScheduleFragment
    private lateinit var transaction: FragmentTransaction
    private lateinit var fragmentManager: FragmentManager

    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setInitialize()
        initView()

    }

    private fun initView() {
        //binding.moimGroupTopbarLayout.layoutTopbarTitleTv.text = "우리 모임"

        binding.moimGroupMoimNameTv.text = thisMoim.moimTitle
        binding.moimGroupMoimExplainTv.text = thisMoim.moimDescription

        val participantDummy = "8"
        binding.moimGroupParticipantTv.text = participantDummy + "명 참여"
        }

    private fun setInitialize() {
        binding = ActivityMoimGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when( intent.getIntExtra("startActivityFlag", -1) ) {
            FLAG_ACTIVITY_MAIN, FLAG_ACTIVITY_VIEWMOIM -> { //main에서 진입 : 새로 모임에 참가 & View moim에서 진입 : 다시 조회
                getMoimInfoFromServer()
                //todo 서버에서 GroupSchedule 데이터 가져와서 적용
            }
            FLAG_ACTIVITY_MAKEMOIM -> { // make moim에서 진입 : 모임 생성
                thisMoim = getMoimInfoFromMakeMoim()
                Log.d("MoimGroupActivity", "thisMoim Info : $thisMoim" )
                setGroupScheduleFragment()
            }
            else -> {
                showDialog("에러 발생", "그룹 시간표 생성중 문제가 발생했습니다.\n다시 시도해 주세요.", "확인")
                finish()
            }
        }

        //~ 명 참여 초기화 필요. 인원 파악 데이터 필요. 일단은 더미로 설정.

        //binding.moimGroupHomeBtn.setOnClickListener(this)
        binding.moimGroupInviteBtn.setOnClickListener(this)
        binding.moimGroupParticipantTv.setOnClickListener(this)
        binding.moimGroupAddPersonalBtn.setOnClickListener(this)
    }

    private fun setGroupScheduleFragment() {
        groupScheduleFragment = GroupScheduleFragment(thisMoim)

        fragmentManager = supportFragmentManager
        transaction = fragmentManager.beginTransaction()
        transaction
            .replace(R.id.moim_group_schedule_fragment, groupScheduleFragment)
            .commitAllowingStateLoss()
    }

    private fun getMoimInfoFromMakeMoim(): Moim {
        if (intent.getStringExtra("moimInfo").isNullOrBlank()) {
            Toast.makeText(this, "모임 생성 에러. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
            finish()
        }
        return gson.fromJson(intent.getStringExtra("moimInfo"), Moim::class.java)
    }

    private fun getMoimInfoFromServer() {
        val moimIdx = intent.getIntExtra("moimIdx", -1)
        if (moimIdx == -1) {
            Toast.makeText(this, "모임 참가 에러. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
            finish()
        }

        getMoim(moimIdx)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id) {

            /*
            R.id.moim_group_home_btn -> { //홈으로 돌아가기. 모임 저장은 생성 및 적용(개인쪽)시에 구현.
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            */
            R.id.moim_group_invite_btn -> { //모임원 초대. 카카오톡 링크로 고민중.
                showInviteDialog()
            }
            R.id.moim_group_participant_tv -> { //참가인원 조회, ~명 참여, 누르면 참가자 명단도 나오도록.
                val dig = ParticipantsDialog(this)
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

    private fun showInviteDialog() {
        val dig = InviteDialog(this, thisMoim.moimIdx, thisMoim.password!!)
        dig.showInviteDialog()
    }

    private fun getMoim( moimIdx: Int ) {

        val retrofitService = RetrofitService()
        retrofitService.setGetMoimView(this)

        Log.d("postGetMoim Active>>>", "sending moimIdx : $moimIdx")
        retrofitService.getMoim( moimIdx )
    }

    override fun onGetMoimLoading() {
        //todo 로딩바 생성
    }

    override fun onGetMoimSuccess(result: GroupScheduleRes) {
        thisMoim.moimIdx = result.moimInfo.moimIdx
        thisMoim.moimTitle = result.moimInfo.moimTitle
        thisMoim.moimDescription = result.moimInfo.moimDescription
        thisMoim.masterUserIdx = result.moimInfo.masterUserIdx
        thisMoim.startTimeHour = result.moimInfo.startTime.toInt() //todo 에러 발생 가능
        thisMoim.endTimeHour = result.moimInfo.endTime.toInt() //todo 에러 발생 가능

        //todo date도 추가 필요
        //todo password 추가 필요 : thisMoim.password
    }

    override fun onGetMoimFailure(code: Int, message: String) {
        Toast.makeText(this, "모임 생성 실패. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
    }
}