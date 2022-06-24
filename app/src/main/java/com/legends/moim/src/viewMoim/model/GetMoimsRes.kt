package com.legends.moim.src.viewMoim.model

import com.google.gson.annotations.SerializedName

data class GetMoimsRes(
    @SerializedName("moimInfo") val moimsInfo: Array<ListMoimInfo>
)

data class ListMoimInfo(
    @SerializedName("moimIdx") val moimIdx: Int,
    @SerializedName("moimTitle") val moimTitle: String,
    @SerializedName("moimDescription") val moimDescription: String
)