package com.legends.moim.config.baseModel

data class Moim(
    var moimIdx : Int = -1,

    var moimTitle: String = "임시 모임",
    var moimDescription: String = "임시로 생성된 모임",

    var masterUserIdx: Int = -1,

    var startTimeHour : Int = 9,
    var endTimeHour: Int = 18,

    val dates: ArrayList<DateStruct> = ArrayList<DateStruct>()
)

data class DateStruct (
    var year : Int,
    var month : Int,
    var day : Int,
    var dayOfWeek : String
)