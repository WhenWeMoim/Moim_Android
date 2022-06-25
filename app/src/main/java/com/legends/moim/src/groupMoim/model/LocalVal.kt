package com.legends.moim.src.groupMoim.model

import com.legends.moim.config.baseModel.Moim
import com.legends.moim.utils.CHOICE_POSSIBLE

//const val CHOICE_LIKE = 1       //선호
//const val CHOICE_POSSIBLE = 2   //가능(Default)
//const val CHOICE_DISLIKE = 3    //비선호
//const val CHOICE_IMPOSSIBLE = 4 //불가능

lateinit var thisMoim: Moim

var selectedBtnFunc: Int = 0

var mySchedule: String? = null