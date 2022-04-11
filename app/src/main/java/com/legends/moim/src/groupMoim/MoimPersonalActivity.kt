package com.legends.moim.src.groupMoim

import android.os.Bundle
import android.view.View
import com.legends.moim.R
import com.legends.moim.config.BaseActivity
import com.legends.moim.databinding.ActivityMoimPersonalBinding

class MoimPersonalActivity: BaseActivity() {

    lateinit var binding: ActivityMoimPersonalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoimPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    fun initView() {
        binding.moimPersonalCompleteBtn.setOnClickListener(this)
        binding.moimPersonalLoadBtn.setOnClickListener(this)
        binding.moimPersonalPreferenceBtn.setOnClickListener(this)
        binding.moimPersonalNonpreferenceBtn.setOnClickListener(this)
        binding.moimPersonalImpossibleBtn.setOnClickListener(this)
        binding.moimPersonalEraserBtn.setOnClickListener(this)
        binding.moimPersonalInitialBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id) {
            R.id.moim_personal_complete_btn -> {
                //내용 적용
                finish()
            }
            R.id.moim_personal_load_btn -> {

            }
            R.id.moim_personal_preference_btn -> {

            }
            R.id.moim_personal_nonpreference_btn -> {

            }
            R.id.moim_personal_impossible_btn -> {

            }
            R.id.moim_personal_eraser_btn -> {

            }
            R.id.moim_personal_initial_btn -> {

            }
        }
    }
}