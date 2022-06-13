package com.legends.moim.utils

import android.os.Build
import android.util.Log
import com.legends.moim.config.baseModel.DateStruct
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

public fun dateStructureConverter(dateStructArray: Array<DateStruct>): Array<String> {
    val resultDateArray = Array<String>( dateStructArray.size) {""}

    for ( i: Int in dateStructArray.indices ) {
        val dateString = String.format("%d-%d-%d", dateStructArray[i].year, dateStructArray[i].month, dateStructArray[i].day)

        resultDateArray[i] = dateString
    }

    Log.d("dateStructureConverter", "Active::: $resultDateArray")
    return resultDateArray
}

public fun dateStringConverter(dateStringArray: Array<String>): Array<DateStruct> {
    val resultStructureArray = Array<DateStruct>( dateStringArray.size) { DateStruct(2000, 0, 0, "월") }

    var dateStringTokens = StringTokenizer(dateStringArray[0], "-")

    var stringYear: Int? = null
    var stringMonth: Int? = null
    var stringDay: Int? = null
    var stringDayOfWeek: String? = null

    for ( i: Int in dateStringArray.indices ) {

        stringYear = dateStringTokens.nextToken().toInt()
        stringMonth = dateStringTokens.nextToken().toInt()
        stringDay = dateStringTokens.nextToken().toInt()
        stringDayOfWeek = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.of( stringYear, stringMonth, stringDay ).dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.KOREAN)
        } else {
            "?"
        }
        val dateStructure = stringDayOfWeek?.let { DateStruct( year = stringYear, month = stringMonth, day = stringDay, dayOfWeek = it) }

        resultStructureArray[i] = dateStructure!!

        dateStringTokens = StringTokenizer(dateStringArray[i], "-")
    }

    Log.d("dateStringConverter", "Active::: $resultStructureArray")
    return resultStructureArray
}