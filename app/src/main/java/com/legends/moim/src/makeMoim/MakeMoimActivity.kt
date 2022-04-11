package com.legends.moim.src.makeMoim

import android.os.Bundle
import android.view.View
import com.legends.moim.R
import com.legends.moim.config.BaseActivity
import com.legends.moim.databinding.ActivityMakeMoimBinding

class MakeMoimActivity: BaseActivity() {

    lateinit var binding : ActivityMakeMoimBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeMoimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    fun initView() {
        binding.makeMoimSelectDateBtn.setOnClickListener(this)
        binding.makeMoimSelectTimeBtn.setOnClickListener(this)
        binding.makeMoimSettingTv.setOnClickListener(this)
        binding.makeMoimCompleteBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id) {
            R.id.make_moim_select_date_btn -> {

            }
            R.id.make_moim_select_time_btn -> {

            }
            R.id.make_moim_setting_tv -> {

            }
            R.id.make_moim_complete_btn -> {

            }

        }
    }
}