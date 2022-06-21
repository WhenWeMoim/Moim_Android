package com.legends.moim.src.makeMoim.model

import com.google.gson.annotations.SerializedName

data class MoimReq(
    @SerializedName("userIdx") var userIdx: Int = -1,
    @SerializedName("moimTitle") var moimTitle: String = "임시 모임",

    @SerializedName("moimDescription") var moimDescription: String = "임시로 생성된 모임",
    @SerializedName("startTime") var startTime : Int = 9,
    @SerializedName("endTime") var endTime: Int = 18,

    @SerializedName("dates") var dates: Array<String>? = null
)
