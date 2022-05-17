package com.legends.moim.utils.retrofit

import retrofit2.Call
import retrofit2.http.*

interface RetrofitInterface {
    @POST("/app/course/{userIdx}")
    fun postPersonalSchedule(@Path("userIdx")userIdx : Int, @Body schedule : String): Call<PostPersonalScheduleResponse>
    //Gson 객체로 바꿔서 서버로 보내는 어노테이션 : @Body

    @GET("/app/trip/{userIdx}/{tripIdx}/courses")
    fun getGroupSchedule(@Path("userIdx") userIdx: Int, @Path("tripIdx") tripIdx: String): Call<GetGroupScheduleResponse>

    @PATCH("/app/trip/deleteTrip/{userIdx}/{tripIdx}")
    fun deleteMoim(@Path("userIdx") userIdx : Int, @Path("moimIdx") moimIdx: Int) : Call<ServerDefaultResponse>

    @PATCH("/app/course/deleteCourse/{userIdx}/{courseIdx}")
    fun deleteCard(@Path("userIdx") userIdx : Int, @Path("courseIdx") courseIdx :String) : Call<ServerDefaultResponse>

    @PATCH("/app/course/courseTitle/{userIdx}/{courseIdx}")
    fun patchPersonalSchedule(@Path("userIdx") userIdx : Int, @Path("moimIdx") moimIdx:Int, @Body params : HashMap<String, Any> ) : Call<ServerDefaultResponse>
}