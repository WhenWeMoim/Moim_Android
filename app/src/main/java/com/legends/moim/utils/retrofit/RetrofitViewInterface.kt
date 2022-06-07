package com.legends.moim.utils.retrofit

interface ServerView {
    fun onServerLoading()
    fun onServerSuccess()
    fun onServerFailure(code : Int, message : String)
}

interface PostMoimView {
    fun onPostMoimLoading()
    fun onPostMoimSuccess(result: Int)
    fun onPostMoimFailure(code : Int, message : String)
}

interface GetMoimsView {
    fun onGetMoimsLoading()
    fun onGetMoimsSuccess(result: String)
    fun onGetMoimsFailure(code : Int, message : String)
}

interface PostPersonalScheduleView {
    fun onPostPersonalScheduleLoading()
    fun onPostPersonalScheduleSuccess(result : String)
    fun onPostPersonalScheduleFailure(code : Int, message : String)
}

interface PostMoimScheduleView {
    fun onPostMoimScheduleLoading()
    fun onPostMoimScheduleSuccess(result : String) //todo 데이터구조 변경 필요
    fun onPostMoimScheduleFailure(code : Int, message : String)
}