package com.legends.moim.src.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.legends.moim.R
import com.legends.moim.config.BaseActivity
import com.legends.moim.databinding.ActivityMainBinding
import com.legends.moim.src.makeMoim.MakeMoimActivity
import com.legends.moim.src.viewMoim.ViewMoimActivity

class MainActivity : BaseActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

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

            }
        }
    }
}