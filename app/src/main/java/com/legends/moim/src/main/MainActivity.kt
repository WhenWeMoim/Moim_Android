package com.legends.moim.src.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.legends.moim.R
import com.legends.moim.config.BaseActivity
import com.legends.moim.databinding.ActivityMainBinding
import com.legends.moim.src.groupMoim.MoimGroupActivity
import com.legends.moim.src.main.model.JoinMoimReq
import com.legends.moim.src.makeMoim.MakeMoimActivity
import com.legends.moim.src.user.UserActivity
import com.legends.moim.src.viewMoim.ViewMoimActivity
import com.legends.moim.utils.FLAG_ACTIVITY_MAIN
import com.legends.moim.utils.getUserIdx
import com.legends.moim.utils.retrofit.RetrofitService
import com.legends.moim.utils.retrofit.ServerView

class MainActivity : BaseActivity(), JoinMoimDialog.JoinMoimDialogClickListener, ServerView {

    lateinit var binding : ActivityMainBinding

    private var moimIdx = -1

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

        val joinMoimInfo = JoinMoimReq( moimIdx = moimIdx, userIdx = getUserIdx(), passwd = moimPw )

        postJoinMoim( joinMoimInfo )
        this.moimIdx = moimIdx
    }

    private fun postJoinMoim( joinMoimReq: JoinMoimReq ) {

        val retrofitService = RetrofitService()
        retrofitService.setServerView(this)

        Log.d("postJoinMoim Active>>>", "sending Model : $joinMoimReq")
        retrofitService.postJoinMoim( joinMoimReq )
    }

    override fun onServerLoading() {
        //todo loding Effect
    }

    override fun onServerSuccess() {
        val intent = Intent(this, MoimGroupActivity::class.java)

        //todo group 데이터 받아서 MoimGroup으로 전송 후 시간표 구성
        intent.putExtra("startActivityFlag", FLAG_ACTIVITY_MAIN)
        intent.putExtra("moimIdx", moimIdx)

        startActivity(intent)
    }

    override fun onServerFailure(code: Int, message: String) {
        Toast.makeText(this, "message", Toast.LENGTH_SHORT).show()
    }
}