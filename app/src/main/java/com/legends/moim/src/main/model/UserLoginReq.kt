package com.legends.moim.src.main.model

import com.google.gson.annotations.SerializedName

data class UserLoginReq(
    @SerializedName("name") val userName: String,
    @SerializedName("token") val loginToken: String
)
