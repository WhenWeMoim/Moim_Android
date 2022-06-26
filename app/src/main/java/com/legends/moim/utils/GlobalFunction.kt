package com.legends.moim.utils

import android.os.Build
import android.util.Log
import android.widget.Toast
import com.legends.moim.config.baseModel.DateStruct
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import kotlin.collections.ArrayList

public fun dateStructure2Int(dateStructArray: ArrayList<DateStruct>): Array<Int> {
    val resultDateArray = Array<Int>( dateStructArray.size) {0}

    for ( i: Int in dateStructArray.indices ) {
        val dateInt = dateStructArray[i].year * 10000 + dateStructArray[i].month * 100 + dateStructArray[i].day

        resultDateArray[i] = dateInt
    }

//    Log.d("dateStructureConverter", "Active::: $resultDateArray")
    return resultDateArray
}

public fun dateInt2Structure(dateIntArray: Array<Int>): ArrayList<DateStruct> {
    val resultDateArrayList = ArrayList<DateStruct>()

    var rawIntDate: Int
    var year: Int
    var month: Int
    var day: Int
    var dayOfWeek: String

    for ( i:Int in dateIntArray.indices ) {
        rawIntDate = dateIntArray[i]
//        Log.d("AAAAAAAAAAAAAAAAA", "rawIntDate : $rawIntDate")
        year = rawIntDate/10000

        rawIntDate %= 10000
        month = rawIntDate/100

        day = rawIntDate %100

//        Log.d("AAAAAAAAAAAAAAAAA", "rawIntDate : $rawIntDate year : $year month : $month, day : $day")

        dayOfWeek = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.of( year, month, day ).dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.KOREAN)
        } else {
            "?"
        }
        resultDateArrayList.add(DateStruct( year = year, month = month, day = day, dayOfWeek = dayOfWeek ))
    }

    return resultDateArrayList
}

public fun scheduleString2IntArray(rawSchedule: String, numOfRow: Int, numOfColumns: Int): Array<IntArray> {
    val resultIntArray = Array(size = numOfRow, init = { IntArray( size = numOfColumns, init = { 2 } ) } )

    for( i in 0 until numOfColumns ) {
        for( j in 0 until numOfRow ) {
            resultIntArray[j][i] = rawSchedule[numOfRow*i+j].digitToInt(10)
        }
    }
    return resultIntArray
}

public fun dateString2Structure(dateStringArray: Array<String>): Array<DateStruct> {
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

//    Log.d("dateStringConverter", "Active::: $resultStructureArray")
    return resultStructureArray
}