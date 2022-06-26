package com.legends.moim.utils.retrofit

import android.util.Log
import com.legends.moim.src.groupMoim.model.PersonalScheduleReq
import com.legends.moim.src.main.model.JoinMoimReq
import com.legends.moim.src.makeMoim.model.PostMoimReq
import com.legends.moim.utils.ApplicationClass.Companion.retrofit
import com.legends.moim.utils.getUserIdx
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitService{

    private lateinit var serverView: ServerView
    fun setServerView(serverView: ServerView) {
        this.serverView = serverView
    }

    private lateinit var loginView: LoginView
    fun setLoginView(loginView: LoginView) {
        this.loginView = loginView
    }

    private lateinit var postMoimView: PostMoimView
    fun setPostMoimView(postMoimView: PostMoimView) {
        this.postMoimView = postMoimView
    }

    private lateinit var getMoimsView: GetMoimsView
    fun setGetMoimsView(getMoimsView: GetMoimsView) {
        this.getMoimsView = getMoimsView
    }

    //특정 모임 정보를 가져왔을때
    private lateinit var getMoimView: GetMoimView
    fun setGetMoimView(getMoimView: GetMoimView) {
        this.getMoimView = getMoimView
    }

    /**
     * 1. 로그인
     */
    fun postLogin( userName: String, userEmail: String ){
        Log.d("CheckPoint : ", "RetrofitService-postLogin Activated")
        loginView.onLoginLoading()

        val params: HashMap<String, String> = HashMap()
        params["userName"] = userName
        params["ps"] = userEmail

        val retrofitService = retrofit.create(RetrofitInterface::class.java)
        retrofitService.postLogin( params ).enqueue(object : Callback<PostLoginResponse> {
            override fun onResponse(call: Call<PostLoginResponse>, response: Response<PostLoginResponse>){
                if (response.isSuccessful) {
                    val res = response.body()!!
                    Log.d("__res", response.body()!!.toString())
                    when (res.code) {
                        1000 -> { //성공
                            Log.d("Retrofit-postLogin", res.code.toString() + " : " + res.message+ "  userIdx : "+ res.result)
                            loginView.onLoginSuccess(res.result)
                        }
                        else -> { //의도된 실패
                            Log.d("Retrofit-postLogin", res.code.toString() + " : " + res.message)
                            loginView.onLoginFailure(res.code, res.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<PostLoginResponse>, t: Throwable) {
                loginView.onLoginFailure(400, t.message.toString())
                Log.d("Retrofit-postLogin", t.toString()) //네트워크 실패
            }
        })
    }

    /**
     * 2-1-1. Moim 생성 -> 서버로 전송
     */
    fun postMoim( postMoimReq: PostMoimReq ){
        Log.d("CheckPoint : ", "RetrofitService-postMoim Activated")
        postMoimView.onPostMoimLoading()

//        val params: HashMap<PostMoimReq> = HashMap()

        val retrofitService = retrofit.create(RetrofitInterface::class.java)
        retrofitService.postMoim(postMoimReq).enqueue(object : Callback<PostMoimResponse> {
            override fun onResponse(call: Call<PostMoimResponse>, response: Response<PostMoimResponse>){
                if (response.isSuccessful) {
                    val res = response.body()!!
                    Log.d("__res", response.body()!!.toString())
                    when (res.code) {
                        1000 -> { //성공
                            Log.d("Retrofit-postMoim", res.code.toString() + " : " + res.message+ "courseIdx : "+ res.result)
                            postMoimView.onPostMoimSuccess( res.result.moimIdx, res.result.pw )
                        }
                        else -> { //의도된 실패
                            Log.d("Retrofit-postMoim", res.code.toString() + " : " + res.message)
                            postMoimView.onPostMoimFailure(res.code, res.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<PostMoimResponse>, t: Throwable) {
                postMoimView.onPostMoimFailure(400, t.message.toString())
                Log.d("Retrofit-postMoim", t.toString()) //네트워크 실패
            }
        })
    }

    /**
     * 2-1-2. 모임 수정
     */

    /**
     * 2-1-3. 서버에서 Moim + GroupSchedule 정보 가져오기
     */
    fun getMoim( moimIdx: Int ){
        val retrofitService = retrofit.create(RetrofitInterface::class.java)

        getMoimView.onGetMoimLoading()

        retrofitService.getMoim(moimIdx).enqueue(object : Callback<GetMoimScheduleResponse> {
            override fun onResponse(call: Call<GetMoimScheduleResponse>, response: Response<GetMoimScheduleResponse>) {
                Log.d("들어오는지 확인", "Retrofit-getMoim")
                if (response.isSuccessful) {
                    val res = response.body()!!
                    Log.d("__res", response.body()!!.toString())
                    when (res.code) {
                        1000 -> {
                            Log.d("Retrofit-getMoim", res.toString())
                            getMoimView.onGetMoimSuccess(res.result)
                        }

                        else -> {
                            Log.d("Retrofit-getMoim", res.toString())
                            getMoimView.onGetMoimFailure(res.code, res.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<GetMoimScheduleResponse>, t: Throwable) {
                Log.d("들어오는지 확인", "CardService-getTrip-onFailure")
                getMoimView.onGetMoimFailure(400, t.message.toString())
            }
        })
    }

    /**
     * 2-2. 내가 소속된 모임들 가져오기
     */
    fun getMoims(){
        val retrofitService = retrofit.create(RetrofitInterface::class.java)

        val userIdx = getUserIdx()
        getMoimsView.onGetMoimsLoading()

        retrofitService.getMoims(userIdx).enqueue(object : Callback<GetMoimsResponse> {
            override fun onResponse(call: Call<GetMoimsResponse>, response: Response<GetMoimsResponse>) {
                if (response.isSuccessful) {
                    val res = response.body()!!
                    Log.d("__res", response.body()!!.toString())
                    when (res.code) {
                        1000 -> {
                            Log.d("Retrofit-getMoims", res.toString())
                            getMoimsView.onGetMoimsSuccess( res.result )
                        }

                        else -> {
                            Log.d("Retrofit-getMoims", res.toString())
                            getMoimsView.onGetMoimsFailure( res.code, res.message )
                        }
                    }
                }
            }
            override fun onFailure(call: Call<GetMoimsResponse>, t: Throwable) {
                getMoimsView.onGetMoimsFailure(400, t.message.toString())
            }
        })
    }

    /**
     * 2-3-1. 이미 post 한 유저의 PersonalSchedule 업로드(patch)
     */
//    fun patchPersonalSchedule( moimIdx: Int, personalSchedule: String ) {
//        Log.d("CheckPoint : ", "Retrofit-patchPersonalSchedule Activated")
//        serverView.onServerLoading()
//
//        val personalScheduleReq=  PersonalScheduleReq(moimIdx = moimIdx, userIdx =  getUserIdx(), schedule = personalSchedule)
//
//        val cardRetrofitService = retrofit.create(RetrofitInterface::class.java)
//        cardRetrofitService.patchPersonalSchedule( personalScheduleReq ).enqueue(object : Callback<ServerDefaultResponse> {
//            override fun onResponse(call: Call<ServerDefaultResponse>, response: Response<ServerDefaultResponse>) {
//                if (response.isSuccessful) {
//                    val res = response.body()!!
//                    Log.d("__res", response.body()!!.toString())
//                    when (res.code) {
//                        1000 -> { //성공
//                            Log.d("Retrofit-PSchedule", res.code.toString() + " : " + res.message)
//                            serverView.onServerSuccess()
//                        }
//                        else -> { //의도된 실패
//                            Log.d("Retrofit-PSchedule", res.code.toString() + " : " + res.message)
//                            serverView.onServerFailure(res.code, res.message)
//                        }
//                    }
//                }
//            }
//            override fun onFailure(call: Call<ServerDefaultResponse>, t: Throwable) {
//                serverView.onServerFailure(400, t.message.toString())
//                Log.d("CardService-patchTitle", t.toString()) //네트워크 실패
//            }
//        })
//    }

    /**
     * 2-3-2. PersonalSchedule 전송
     */
    fun postPersonalSchedule( moimIdx: Int, schedule: String ){
        Log.d("CheckPoint : ", "CardService-postCard Activated")
        serverView.onServerLoading()

        val personalScheduleReq=  PersonalScheduleReq(moimIdx = moimIdx, userIdx =  getUserIdx(), schedule = schedule)

        val cardRetrofitService = retrofit.create(RetrofitInterface::class.java)
        cardRetrofitService.patchPersonalSchedule(personalScheduleReq).enqueue(object : Callback<ServerDefaultResponse> {
            override fun onResponse(call: Call<ServerDefaultResponse>, response: Response<ServerDefaultResponse>){
                if (response.isSuccessful) {
                    val res = response.body()!!
                    Log.d("__res", response.body()!!.toString())
                    when (res.code) {
                        1000 -> { //성공
                            Log.d("CardService-postCard", res.code.toString() + " : " + res.message)
                            serverView.onServerSuccess()
                        }
                        else -> { //의도된 실패
                            Log.d("CardService-postCard", res.code.toString() + " : " + res.message)
                            serverView.onServerFailure(res.code, res.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ServerDefaultResponse>, t: Throwable) {
                serverView.onServerFailure(400, t.message.toString())
                Log.d("CardService-postCard", t.toString()) //네트워크 실패
            }
        })
    }

    /**
     * 3-1. 모임 참가
     */
    fun postJoinMoim( joinMoimReq: JoinMoimReq ){
        Log.d("CheckPoint ::: ", "RetrofitService-postJoinMoim Activated")
        serverView.onServerLoading()

        val retrofitService = retrofit.create(RetrofitInterface::class.java)
        retrofitService.postJoinMoim( joinMoimReq ).enqueue(object : Callback<ServerDefaultResponse> {
            override fun onResponse(call: Call<ServerDefaultResponse>, response: Response<ServerDefaultResponse>){
                if (response.isSuccessful) {
                    val res = response.body()!!
                    Log.d("__res", response.body()!!.toString())
                    when (res.code) {
                        1000 -> { //성공
                            Log.d("Retrofit-postJoinMoim", res.code.toString() + " : " + res.message)
                            serverView.onServerSuccess()
                        }
                        else -> { //의도된 실패
                            Log.d("Retrofit-postJoinMoim", res.code.toString() + " : " + res.message)
                            serverView.onServerFailure(res.code, res.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ServerDefaultResponse>, t: Throwable) {
                serverView.onServerFailure(400, t.message.toString())
                Log.d("Retrofit-postJoinMoim", t.toString()) //네트워크 실패
            }
        })
    }

    /**
     *  (아직 구현 안됨) - 모임 삭제
     */
//    fun deleteMoim(moimIdx: Int) {
//        Log.d("CheckPoint : ", "CardService-deleteTrip Activated")
//
//        val userIdx = getUserIdx()
//
//        serverView.onServerLoading()
//
//        val retrofitService = retrofit.create(RetrofitInterface::class.java)
//        retrofitService.deleteMoim(userIdx, moimIdx).enqueue(object : Callback<ServerDefaultResponse> {
//            override fun onResponse(call: Call<ServerDefaultResponse>, response: Response<ServerDefaultResponse>) {
//                if (response.isSuccessful) {
//                    val res = response.body()!!
//                    Log.d("__res", response.body()!!.toString())
//                    when (res.code) {
//                        1000 -> { //성공
//                            Log.d("CardService-deleteTrip", res.code.toString() + " : " + res.message)
//                            serverView.onServerSuccess()
//                        }
//                        else -> { //의도된 실패
//                            Log.d("CardService-deleteTrip", res.code.toString() + " : " + res.message)
//                            serverView.onServerFailure(res.code, res.message)
//                        }
//                    }
//                }
//            }
//            override fun onFailure(call: Call<ServerDefaultResponse>, t: Throwable) {
//                serverView.onServerFailure(400, t.message.toString())
//                Log.d("CardService-deleteTrip", t.toString()) //네트워크 실패
//            }
//        })
//    }

}
