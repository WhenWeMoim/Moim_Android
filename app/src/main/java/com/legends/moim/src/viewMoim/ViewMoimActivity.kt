package com.legends.moim.src.viewMoim

import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.legends.moim.config.BaseActivity
import com.legends.moim.databinding.ActivityViewMoimBinding
import com.legends.moim.src.groupMoim.MoimGroupActivity
import com.legends.moim.src.makeMoim.MakeMoimActivity
import com.legends.moim.src.viewMoim.model.Moim

class ViewMoimActivity: BaseActivity() {

    lateinit var binding : ActivityViewMoimBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewMoimBinding.inflate(layoutInflater)

        initView()
    }

    private fun initView() {
        val moimRVAdapter = RVMoimsAdapter()

        binding.viewMoimRV.adapter = moimRVAdapter
        binding.viewMoimRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        moimRVAdapter.setMoimClickListener(object : RVMoimsAdapter.MoimClickListener{
            override fun onItemClick(moim: Moim) {
                val intent = Intent(applicationContext, MoimGroupActivity::class.java)

                TODO("해당한 position의 moim 정보를 intent에 넣어서 보내주든, 서버에서 받아오든 해야 함")

                startActivity(intent)
            }
        })
    }
}