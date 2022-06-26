package com.legends.moim.src.groupMoim.model

import com.google.gson.annotations.SerializedName

data class GroupScheduleRes(
    @SerializedName("moimInfo")val moimInfo: MoimInfo,
    @SerializedName("dates")val dates: Array<Int>,
    @SerializedName("moimUserSchedules")val userSchedules: Array<UserSchedules>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GroupScheduleRes

        if (moimInfo != other.moimInfo) return false
        if (!dates.contentEquals(other.dates)) return false
        if (!userSchedules.contentEquals(other.userSchedules)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = moimInfo.hashCode()
        result = 31 * result + dates.contentHashCode()
        result = 31 * result + userSchedules.contentHashCode()
        return result
    }
}

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
    @SerializedName("schedules") var schedules: String?
)

//data class UserSchedule(
//    @SerializedName("dates") val dates: String, //"2022-05-03"
//    @SerializedName("schedules") val schedules: String //"112"
//)
