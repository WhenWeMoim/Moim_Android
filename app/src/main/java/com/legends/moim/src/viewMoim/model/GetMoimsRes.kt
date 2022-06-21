package com.legends.moim.src.viewMoim.model

import com.google.gson.annotations.SerializedName

data class GetMoimsRes(
    @SerializedName("moimInfo") val moimInfo: Array<ListMoimInfo>
)

data class ListMoimInfo(
    @SerializedName("moimInfo") val moimIdx: Int,
    @SerializedName("moimTitle") val moimTitle: String,
    @SerializedName("moimDescription") val moimDescription: String
)