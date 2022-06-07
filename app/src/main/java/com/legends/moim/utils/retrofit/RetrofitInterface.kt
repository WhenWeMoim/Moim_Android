package com.legends.moim.utils.retrofit

import retrofit2.Call
import retrofit2.http.*

interface RetrofitInterface {

    //회원가입(카카오) - 최초 앱 실행 1회
    @POST("/app/course/{userIdx}")
    fun postSignup(@Body token : String): Call<PostLoginResponse>

    //로그인 - 앱 삭제 후 앱 실행
    @POST("/app/course/{userIdx}")
    fun postLogin(@Body token : String): Call<PostLoginResponse>

    //모임 참가
    @POST("/app/course/{userIdx}")
    fun postJoinMoim(@Body moimIdx: Int, @Body moimPw: Int): Call<PostLoginResponse>

    //나의 모임들 조회
    @GET("/app/trip/{userIdx}/{tripIdx}/courses")
    fun getMyMoims(@Path("userIdx") userIdx: Int, @Path("tripIdx") tripIdx: String): Call<GetMyMoimsResponse>

    //모임 생성(전송)
    @POST("/app/course/{userIdx}")
    fun postMoim(@Path("userIdx")userIdx : Int, @Body schedule : String): Call<PostPersonalScheduleResponse>

    //모임 개인 시간표 적용(전송)
    @POST("/app/course/{userIdx}")
    fun postPersonalSchedule(@Path("userIdx")userIdx : Int, @Body schedule : String): Call<PostPersonalScheduleResponse>
    //Gson 객체로 바꿔서 서버로 보내는 어노테이션 : @Body

    //모임의 그룹 시간표 정보 가져오기
    @GET("/app/trip/{userIdx}/{tripIdx}/courses")
    fun getGroupSchedule(@Path("userIdx") userIdx: Int, @Path("tripIdx") tripIdx: String): Call<GetGroupScheduleResponse>

    //모임 개인 시간표 수정
    @PATCH("/app/course/courseTitle/{userIdx}/{courseIdx}")
    fun patchPersonalSchedule(@Path("userIdx") userIdx : Int, @Path("moimIdx") moimIdx:Int, @Body params : HashMap<String, Any> ) : Call<ServerDefaultResponse>

    /*----- 아직 구현 안됨 -----*/

    //모임 삭제
    @PATCH("/app/trip/deleteTrip/{userIdx}/{tripIdx}")
    fun deleteMoim(@Path("userIdx") userIdx : Int, @Path("moimIdx") moimIdx: Int) : Call<ServerDefaultResponse>

    //모임 수정
    @PATCH("/app/trip/deleteTrip/{userIdx}/{tripIdx}")
    fun patchMoim(@Path("userIdx") userIdx : Int, @Path("moimIdx") moimIdx: Int) : Call<ServerDefaultResponse>

    @PATCH("/app/course/deleteCourse/{userIdx}/{courseIdx}")
    fun deleteCard(@Path("userIdx") userIdx : Int, @Path("courseIdx") courseIdx :String) : Call<ServerDefaultResponse>
}