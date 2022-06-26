package com.legends.moim.src.groupMoim.model

import com.google.gson.annotations.SerializedName

data class GroupScheduleRes(
    @SerializedName("moimInfo")val moimInfo: MoimInfo,
    @SerializedName("dates")val dates: Array<Int>,
    @SerializedName("moimUserSchedules")val userSchedules: Array<UserSchedules>
)

data class MoimInfo (
    @SerializedName("moimIdx") val moimIdx: Int,
    @SerializedName("moimTitle") val moimTitle: String,
    @SerializedName("moimDescription") val moimDescription: String,
    @SerializedName("masterUserIdx") val masterUserIdx: Int,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("passwd") val passwd: String
)
data class UserSchedules (
    @SerializedName("userName") val userName: String,
    @SerializedName("schedules") val schedules: String?
)

//data class UserSchedule(
//    @SerializedName("dates") val dates: String, //"2022-05-03"
//    @SerializedName("schedules") val schedules: String //"112"
//)
