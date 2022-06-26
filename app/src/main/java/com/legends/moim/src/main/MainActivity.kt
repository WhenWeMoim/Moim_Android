package com.legends.moim.src.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.legends.moim.R
import com.legends.moim.config.BaseActivity
import com.legends.moim.config.baseModel.Moim
import com.legends.moim.databinding.ActivityMainBinding
import com.legends.moim.src.groupMoim.GroupMoimActivity
import com.legends.moim.src.groupMoim.model.GroupScheduleRes
import com.legends.moim.src.main.model.JoinMoimReq
import com.legends.moim.src.makeMoim.MakeMoimActivity
import com.legends.moim.src.user.UserActivity
import com.legends.moim.src.viewMoim.ViewMoimActivity
import com.legends.moim.utils.FLAG_ACTIVITY_MAIN
import com.legends.moim.utils.dateInt2Structure
import com.legends.moim.utils.getUserIdx
import com.legends.moim.utils.retrofit.GetMoimView
import com.legends.moim.utils.retrofit.RetrofitService
import com.legends.moim.utils.retrofit.ServerView

class MainActivity : BaseActivity(), JoinMoimDialog.JoinMoimDialogClickListener, ServerView, GetMoimView {

    lateinit var binding : ActivityMainBinding

    private var moimIdx = -1

    private val retrofitService = RetrofitService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        binding.mainMakeMoimBtn.setOnClickListener(this)
        binding.mainViewMoimBtn.setOnClickListener(this)
        binding.mainUserBtn.setOnClickListener(this)
        binding.mainJoinMoimBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id) {
            R.id.main_make_moim_btn -> { //MakeMoimActivity로 이동
                val intent = Intent(this, MakeMoimActivity::class.java)
                startActivity(intent)
            }
            R.id.main_view_moim_btn -> { //ViewMoimActivity로 이동
                val intent = Intent(this, ViewMoimActivity::class.java)
                startActivity(intent)
            }
            R.id.main_user_btn -> {
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
            }
            R.id.main_join_moim_btn -> {
                showJoinMoimDialog()
            }
        }
    }

    private fun showJoinMoimDialog() {
        val dig = JoinMoimDialog(this)
        dig.listener = this
        dig.showJoinMoimDialog()
    }

    override fun onJoinMoimDialogOKClicked(moimIdx: Int?, moimPw: String?) {
        if ( moimIdx == null ) return

        Log.d(">>>>>>>>>>>>>>>>>>>>", "moimIdx : $moimIdx, moimPw : $moimPw")
        this.moimIdx = moimIdx
        postJoinMoim( JoinMoimReq( moimIdx = moimIdx, userIdx = getUserIdx(), passwd = moimPw ) )
    }

    private fun postJoinMoim( joinMoimReq: JoinMoimReq ) {
        retrofitService.setServerView(this)

        Log.d("postJoinMoim Active>>>", "sending Model : $joinMoimReq")
        retrofitService.postJoinMoim( joinMoimReq )
    }

    private fun getMoim( moimIdx: Int ) {
        retrofitService.setGetMoimView(this)

        Log.d("postGetMoim Active>>>", "sending moimIdx : $moimIdx")
        retrofitService.getMoim( moimIdx )
    }


    override fun onServerLoading() {
        //todo loding Effect
    }

    override fun onServerSuccess() {
        Log.d("postGetMoim Success>>>", "nowMoimIdx: $moimIdx")
        getMoim(moimIdx)
    }

    override fun onServerFailure(code: Int, message: String) {
        Toast.makeText(this, "$code $message\n모임 불러오기 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
    }

    override fun onGetMoimLoading() {
        //TODO("Not yet implemented")
    }

    override fun onGetMoimSuccess(result: GroupScheduleRes) {
        val dates = dateInt2Structure(result.dates)
        val thisMoim = Moim(
            moimIdx = result.moimInfo.moimIdx,
            moimTitle = result.moimInfo.moimTitle,
            moimDescription = result.moimInfo.moimDescription,
            masterUserIdx = result.moimInfo.masterUserIdx,
            startTimeHour = result.moimInfo.startTime.toInt(),
            endTimeHour = result.moimInfo.endTime.toInt(),
            dates = dates,
            password = result.moimInfo.passwd
        )

        val intent = Intent(this, GroupMoimActivity::class.java)

        val gson = Gson()
        intent.putExtra("startActivityFlag", FLAG_ACTIVITY_MAIN)
        intent.putExtra("moimInfo",  gson.toJson(thisMoim))
        intent.putExtra("moimSchedule", gson.toJson(result.userSchedules))

        startActivity(intent)
    }

    override fun onGetMoimFailure(code: Int, message: String) {
        Toast.makeText(this, "$code 모임 불러오기 실패. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
    }
}