package com.legends.moim.utils.retrofit

interface ServerView {
    fun onServerLoading()
    fun onServerSuccess()
    fun onServerFailure(code : Int, message : String)
}

interface PostPersonalScheduleView {
    fun onPostPersonalScheduleLoading()
    fun onPostPersonalScheduleSuccess(result : String)
    fun onPostPersonalScheduleFailure(code : Int, message : String)
}

interface PostGroupScheduleView {
    fun onPostGroupScheduleLoading()
    fun onPostGroupScheduleSuccess(result : String)
    fun onPostGroupScheduleFailure(code : Int, message : String)
}