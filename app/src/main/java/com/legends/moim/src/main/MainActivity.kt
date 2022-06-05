package com.legends.moim.src.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.legends.moim.R
import com.legends.moim.config.BaseActivity
import com.legends.moim.config.baseModel.Moim
import com.legends.moim.databinding.ActivityMainBinding
import com.legends.moim.src.makeMoim.MakeMoimActivity
import com.legends.moim.src.makeMoim.dialog.TimeDialog
import com.legends.moim.src.user.UserActivity
import com.legends.moim.src.viewMoim.ViewMoimActivity

class MainActivity : BaseActivity(), JoinMoimDialog.JoinMoimDialogClickListener {

    lateinit var binding : ActivityMainBinding

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
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id) {
            R.id.main_make_moim_btn -> { //MmakeMoimActivity로 이동
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

    override fun onJoinMoimDialogOKClicked(moimIdx: Int, moimPw: Int) {
        //TODO 모임 정보 서버로 전송 + 일치하면 모임 정보도 가져옴
    }
}