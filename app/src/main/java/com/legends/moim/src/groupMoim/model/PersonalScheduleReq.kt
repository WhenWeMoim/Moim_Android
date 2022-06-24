package com.legends.moim.src.groupMoim.model

import com.google.gson.annotations.SerializedName

data class PersonalScheduleReq(
    @SerializedName("moimIdx") val moimIdx: Int,
    @SerializedName("userIdx") val userIdx: Int,
    @SerializedName("schedule") val schedule: String
)
