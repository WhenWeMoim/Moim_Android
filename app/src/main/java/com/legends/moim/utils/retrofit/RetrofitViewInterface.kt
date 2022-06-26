package com.legends.moim.utils.retrofit

import com.legends.moim.src.groupMoim.model.GroupScheduleRes
import com.legends.moim.src.viewMoim.model.GetMoimsRes

interface ServerView {
    fun onServerLoading()
    fun onServerSuccess()
    fun onServerFailure(code : Int, message : String)
}

interface LoginView {
    fun onLoginLoading()
    fun onLoginSuccess(result: Int)
    fun onLoginFailure(code : Int, message : String)
}

interface PostMoimView {
    fun onPostMoimLoading()
    fun onPostMoimSuccess(moimIdx: Int, pw: String)
    fun onPostMoimFailure(code : Int, message : String)
}

interface GetMoimView {
    fun onGetMoimLoading()
    fun onGetMoimSuccess(result: GroupScheduleRes)
    fun onGetMoimFailure(code : Int, message : String)
}

interface GetMoimsView {
    fun onGetMoimsLoading()
    fun onGetMoimsSuccess(result: GetMoimsRes)
    fun onGetMoimsFailure(code : Int, message : String)
}

interface PostMoimScheduleView {
    fun onPostMoimScheduleLoading()
    fun onPostMoimScheduleSuccess(result : String) //todo 데이터구조 변경 필요
    fun onPostMoimScheduleFailure(code : Int, message : String)
}