package com.legends.moim.src.makeMoim.model

data class Moim(
    private var index : Int = -1,

    var title: String = "임시 모임",
    var startTimeHour : Int = 9,
    var startTimeMin: Int = 0,
    var endTimeHour: Int = 18,
    var endTimeMin: Int = 0,

    val dates: ArrayList<Date>
)

data class Date(
    var year: Int= -1,
    var month: Int= -1,
    var day: Int= -1
)
