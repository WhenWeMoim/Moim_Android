package com.legends.moim.src.makeMoim.model

import com.google.gson.annotations.SerializedName

data class MoimReq(
    @SerializedName("userIdx") var userIdx: Int = -1,
    @SerializedName("moimTitle") var title: String = "임시 모임",

    @SerializedName("moimDescription") var explain: String = "임시로 생성된 모임",
    @SerializedName("startTime") var startTimeHour : Int = 9,
    @SerializedName("endTime") var endTimeHour: Int = 18,

    @SerializedName("dates") var dates: ArrayList<String> = ArrayList<String>()
)
