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
        setContentView(binding.root)

        //dummy test Function todo delete
        //getDummyMoims()

        getMoims()
    }

    private fun getDummyMoims() {
        val dummyGetMoimsRes = arrayOf(
            ListMoimInfo(moimIdx = -1, moimTitle = "모임 1", moimDescription = "모임 1에 대한 설명"),
            ListMoimInfo(moimIdx = -1, moimTitle = "모임 2", moimDescription = "모임 2에 대한 설명"),
            ListMoimInfo(moimIdx = -1, moimTitle = "모임 3", moimDescription = "모임 3에 대한 설명"),
            ListMoimInfo(moimIdx = -1, moimTitle = "모임 4", moimDescription = "모임 4에 대한 설명"),
            ListMoimInfo(moimIdx = -1, moimTitle = "모임 5", moimDescription = "모임 5에 대한 설명"), //연장시켜볼까?
            ListMoimInfo(moimIdx = -1, moimTitle = "모임 d6", moimDescription = "모임 6에 대한 설명"),
            ListMoimInfo(moimIdx = -1, moimTitle = "모임 d7", moimDescription = "모임 7에 대한 설명"),
            ListMoimInfo(moimIdx = -1, moimTitle = "모임 d8", moimDescription = "모임 8에 대한 설명"),
            ListMoimInfo(moimIdx = -1, moimTitle = "모임 d9", moimDescription = "모임 9에 대한 설명")

        )

        initRVMoimsAdapter(dummyGetMoimsRes)
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
        initRVMoimsAdapter(result.moimsInfo)
    }

    private fun initRVMoimsAdapter(moimsInfo: Array<ListMoimInfo>) {
        val moimRVAdapter = RVMoimsAdapter(moimsInfo)

        binding.viewMoimRV.adapter = moimRVAdapter
        binding.viewMoimRV.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        moimRVAdapter.setMoimClickListener(object : RVMoimsAdapter.MoimClickListener {
            override fun onItemClick(listMoimInfo: ListMoimInfo) {
                val intent = Intent(applicationContext, MoimGroupActivity::class.java)

                intent.putExtra("startActivityFlag", FLAG_ACTIVITY_VIEWMOIM)
                intent.putExtra("moimIdx", listMoimInfo.moimIdx)
                //TODO("해당한 position의 moim 정보를 intent에 넣어서 보내주든, 서버에서 받아오든 해야 함")

                startActivity(intent)
            }
        })
    }

    override fun onGetMoimsFailure(code: Int, message: String) {
        Toast.makeText(this, "$code 모임 조회 실패. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
    }
}