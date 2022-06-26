package com.legends.moim.src.viewMoim

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.legends.moim.R
import com.legends.moim.config.BaseActivity
import com.legends.moim.config.baseModel.Moim
import com.legends.moim.databinding.ActivityViewMoimBinding
import com.legends.moim.src.groupMoim.GroupMoimActivity
import com.legends.moim.src.groupMoim.model.GroupScheduleRes
import com.legends.moim.src.makeMoim.MakeMoimActivity
import com.legends.moim.src.viewMoim.model.GetMoimsRes
import com.legends.moim.src.viewMoim.model.ListMoimInfo
import com.legends.moim.utils.FLAG_ACTIVITY_VIEWMOIM
import com.legends.moim.utils.dateInt2Structure
import com.legends.moim.utils.retrofit.GetMoimView
import com.legends.moim.utils.retrofit.GetMoimsView
import com.legends.moim.utils.retrofit.RetrofitService

class ViewMoimActivity: BaseActivity(), GetMoimsView, GetMoimView {

    lateinit var binding : ActivityViewMoimBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewMoimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewMoimFirstMsgTv.setOnClickListener (this)
    }

    override fun onResume() {
        super.onResume()
        getMoims()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id) {
            R.id.view_moim_first_msg_tv -> {
                val intent = Intent(this, MakeMoimActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun initRVMoimsAdapter(moimsInfo: Array<ListMoimInfo>) {
        val moimRVAdapter = RVMoimsAdapter(moimsInfo)

        binding.viewMoimRV.adapter = moimRVAdapter
        binding.viewMoimRV.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        moimRVAdapter.setMoimClickListener(object : RVMoimsAdapter.MoimClickListener {
            override fun onItemClick(listMoimInfo: ListMoimInfo) {
                getMoimInfoFromServer(listMoimInfo.moimIdx)
            }
        })
    }

    private fun getMoims() {
        val retrofitService = RetrofitService()
        retrofitService.setGetMoimsView(this)

        retrofitService.getMoims()
    }

    private fun getMoimInfoFromServer(moimIdx: Int) {
        if (moimIdx == -1) {
            Toast.makeText(this, "모임 참가 에러. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
            finish()
        }

        getMoim(moimIdx)
    }

    private fun getMoim( moimIdx: Int ) {

        val retrofitService = RetrofitService()
        retrofitService.setGetMoimView(this)

        Log.d("postGetMoim Active>>>", "sending moimIdx : $moimIdx")
        retrofitService.getMoim( moimIdx )
    }

    override fun onGetMoimsLoading() {
        //TODO("Not yet implemented")
    }

    override fun onGetMoimsSuccess( result: GetMoimsRes ) {
        if( result.moimsInfo.isEmpty() ) {
            binding.viewMoimFirstMsgTv.visibility = View.VISIBLE
            binding.viewMoimRV.visibility = View.GONE
        } else {
            binding.viewMoimFirstMsgTv.visibility = View.GONE
            binding.viewMoimRV.visibility = View.VISIBLE
            initRVMoimsAdapter(result.moimsInfo)
        }
    }

    override fun onGetMoimsFailure(code: Int, message: String) {
        Toast.makeText(this, "$code 모임 조회 실패. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
    }

    override fun onGetMoimLoading() {
        //TODO("Not yet implemented")
    }

    override fun onGetMoimSuccess(result: GroupScheduleRes) {
        val intent = Intent(this, GroupMoimActivity::class.java)

        intent.putExtra("startActivityFlag", FLAG_ACTIVITY_VIEWMOIM)

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

        val gson = Gson()
        intent.putExtra("moimInfo",  gson.toJson(thisMoim))
        intent.putExtra("moimSchedule", gson.toJson(result.userSchedules))

        startActivity(intent)
    }

    override fun onGetMoimFailure(code: Int, message: String) {
        Toast.makeText(this, "$code 모임 불러오기 실패. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
    }
}