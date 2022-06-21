package com.legends.moim.src.main.model

import com.google.gson.annotations.SerializedName

data class JoinMoimReq(
    @SerializedName("moimIdx") val moimIdx: Int,
    @SerializedName("userIdx") val userIdx: Int,
    @SerializedName("passwd") var passwd: String? = null
)
