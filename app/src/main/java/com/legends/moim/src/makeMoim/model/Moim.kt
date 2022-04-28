package com.legends.moim.src.makeMoim.model

data class Moim(
    private var index : Int = -1,

    var title: String = "임시 모임",
    var description: String = "임시로 생성된 모임",

    var startTimeHour : Int = 9,
    var endTimeHour: Int = 18,

    val dates: ArrayList<SelectedDate> = ArrayList<SelectedDate>()

//    var endTimeMin: Int = 0,
//    var startTimeMin: Int = 0,
//    var interval: Int = 60,
)

data class SelectedDate(
    var year: Int,
    var month: Int,
    var day: Int
)
