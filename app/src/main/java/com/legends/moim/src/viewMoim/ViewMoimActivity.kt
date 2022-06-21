package com.legends.moim.src.viewMoim

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.legends.moim.config.BaseActivity
import com.legends.moim.databinding.ActivityViewMoimBinding
import com.legends.moim.src.groupMoim.MoimGroupActivity
import com.legends.moim.src.viewMoim.model.GetMoimsRes
import com.legends.moim.src.viewMoim.model.ListMoimInfo
import com.legends.moim.utils.FLAG_ACTIVITY_VIEWMOIM
import com.legends.moim.utils.retrofit.GetMoimsView
import com.legends.moim.utils.retrofit.RetrofitService

class ViewMoimActivity: BaseActivity(), GetMoimsView {

    lateinit var binding : ActivityViewMoimBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewMoimBinding.inflate(layoutInflater)

        getMoims()
    }

    private fun getMoims() {
        val retrofitService = RetrofitService()
        retrofitService.setGetMoimsView(this)

        retrofitService.getMoims()
    }

    override fun onGetMoimsLoading() {
        //TODO("Not yet implemented")
    }

    override fun onGetMoimsSuccess( result: GetMoimsRes ) {
        val moimRVAdapter = RVMoimsAdapter( result.moimInfo )

        binding.viewMoimRV.adapter = moimRVAdapter
        binding.viewMoimRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        moimRVAdapter.setMoimClickListener(object : RVMoimsAdapter.MoimClickListener{
            override fun onItemClick( moimInfo: ListMoimInfo ) {
                val intent = Intent(applicationContext, MoimGroupActivity::class.java)

                intent.putExtra("startDivideFlag", FLAG_ACTIVITY_VIEWMOIM)
                intent.putExtra("moimIdx", moimInfo.moimIdx)
                //TODO("해당한 position의 moim 정보를 intent에 넣어서 보내주든, 서버에서 받아오든 해야 함")

                startActivity(intent)
            }
        })
    }

    override fun onGetMoimsFailure(code: Int, message: String) {
        Toast.makeText(this, "$code 모임 생성 실패. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
    }
}