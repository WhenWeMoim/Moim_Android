package com.legends.moim.utils.retrofit

import com.legends.moim.src.main.model.JoinMoimReq
import com.legends.moim.src.makeMoim.model.PostMoimReq
import retrofit2.Call
import retrofit2.http.*

interface RetrofitInterface {

    //회원가입(카카오) - 최초 앱 실행 1회
    @POST("/users/signup")
    fun postSignup(@Body token : String): Call<PostUserResponse>

    //로그인 - 앱 삭제 후 앱 실행
    @POST("/users/login")
    fun postLogin(@Body params : HashMap<String, String>): Call<PostLoginResponse>

    //모임 참가
    @POST("/moimUsers")
    fun postJoinMoim(@Body joinMoimReq: JoinMoimReq): Call<ServerDefaultResponse>

    //나의 모임들 조회
    @GET("/moims/moims/{userIdx}")
    fun getMoims(@Path("userIdx") userIdx: Int): Call<GetMoimsResponse>

    //모임 생성(전송)
    @POST("/moims")
    fun postMoim(@Body postMoimReq: PostMoimReq): Call<PostMoimResponse>

    //모임의 모임정보, 모임 시간표 가져오기
    @GET("/moims/{moimIdx}")
    fun getMoim(@Path("moimIdx") moimIdx: Int): Call<GetMoimScheduleResponse>

    //모임 개인 시간표 적용(전송)
//    @POST("/app/course/{userIdx}")
//    fun postPersonalSchedule(@Path("userIdx")userIdx : Int, @Body schedule : String): Call<ServerDefaultResponse>
    //Gson 객체로 바꿔서 서버로 보내는 어노테이션 : @Body

    //모임 개인 시간표 수정
    @PATCH("/moims/{moimIdx}/{userIdx}/{schedule}")
    fun patchPersonalSchedule(@Path("moimIdx") moimIdx:Int, @Path("userIdx") userIdx : Int, @Path("schedule")schedule : String ) : Call<ServerDefaultResponse>

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