package com.legends.moim.utils.retrofit

import android.util.Log
import com.legends.moim.src.makeMoim.model.MoimReq
import com.legends.moim.utils.ApplicationClass.Companion.retrofit
import com.legends.moim.utils.getUserIdx
import retrofit2.*

class RetrofitService{

    private lateinit var serverView: ServerView
    fun setServerView(serverView: ServerView) {
        this.serverView = serverView
    }

    private lateinit var postMoimView: PostMoimView
    fun setPostMoimView(postMoimView: PostMoimView) {
        this.postMoimView = postMoimView
    }

    private lateinit var getMoimsView: GetMoimsView
    fun setGetMoimsView(getMoimsView: GetMoimsView) {
        this.getMoimsView = getMoimsView
    }

    private lateinit var postPersonalScheduleView : PostPersonalScheduleView
    fun setCardsView(postPersonalScheduleView : PostPersonalScheduleView) {
        this.postPersonalScheduleView = postPersonalScheduleView
    }

    private lateinit var postMoimScheduleView : PostMoimScheduleView
    fun setPostMoimScheduleView(postMoimScheduleView : PostMoimScheduleView) {
        this.postMoimScheduleView = postMoimScheduleView
    }

    /**
     * 1-1. Moim 생성 -> 서버로 전송
     */
    fun postMoim( moimReq: MoimReq ){
        Log.d("CheckPoint : ", "RetrofitService-postMoim Activated")
        postMoimView.onPostMoimLoading()

        moimReq.userIdx = getUserIdx()

        val retrofitService = retrofit.create(RetrofitInterface::class.java)
        retrofitService.postMoim(moimReq).enqueue(object : Callback<PostMoimResponse> {
            override fun onResponse(call: Call<PostMoimResponse>, response: Response<PostMoimResponse>){
                if (response.isSuccessful) {
                    val res = response.body()!!
                    Log.d("__res", response.body()!!.toString())
                    when (res.code) {
                        1000 -> { //성공
                            Log.d("Retrofit-postMoim", res.code.toString() + " : " + res.message+ "courseIdx : "+ res.result)
                            postMoimView.onPostMoimSuccess( res.result.moimIdx )
                        }
                        else -> { //의도된 실패
                            Log.d("Retrofit-postMoim", res.code.toString() + " : " + res.message)
                            postMoimView.onPostMoimFailure(res.code, res.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<PostMoimResponse>, t: Throwable) {
                postPersonalScheduleView.onPostPersonalScheduleFailure(400, t.message.toString())
                Log.d("Retrofit-postMoim", t.toString()) //네트워크 실패
            }
        })
    }

    /**
     * 1-3. 서버에서 Moim + GroupSchedule 정보 가져오기
     */
    fun getMoimSchedule( moimIdx: Int ){
        val retrofitService = retrofit.create(RetrofitInterface::class.java)

        postMoimScheduleView.onPostMoimScheduleLoading()

        retrofitService.getMoimSchedule(moimIdx).enqueue(object : Callback<GetMoimScheduleResponse> {
            override fun onResponse(call: Call<GetMoimScheduleResponse>, response: Response<GetMoimScheduleResponse>) {
                Log.d("들어오는지 확인", "Retrofit-getMoimSchdule")
                if (response.isSuccessful) {
                    val res = response.body()!!
                    Log.d("__res", response.body()!!.toString())
                    when (res.code) {
                        1000 -> {
                            Log.d("Retrofit-getMoimSchdule", res.toString())
                            postMoimScheduleView.onPostMoimScheduleSuccess(res.result)
                        }

                        else -> {
                            Log.d("Retrofit-getMoimSchdule", res.toString())
                            postMoimScheduleView.onPostMoimScheduleFailure(res.code, res.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<GetMoimScheduleResponse>, t: Throwable) {
                Log.d("들어오는지 확인", "CardService-getTrip-onFailure")
                postMoimScheduleView.onPostMoimScheduleFailure(400, t.message.toString())
            }
        })
    }

    /**
     * 이미 post 한 유저의 PersonalSchedule 업로드(patch)
     */
    fun patchPersonalSchedule(moimIdx: Int, personalSchedule: String) {
        Log.d("CheckPoint : ", "CardService-patchTitle Activated")
        serverView.onServerLoading()
        val params: HashMap<String, Any> = HashMap()
        params["courseTitle"] = personalSchedule

        val userIdx = getUserIdx()

        val cardRetrofitService = retrofit.create(RetrofitInterface::class.java)
        cardRetrofitService.patchPersonalSchedule(userIdx, moimIdx, params).enqueue(object : Callback<ServerDefaultResponse> {
            override fun onResponse(call: Call<ServerDefaultResponse>, response: Response<ServerDefaultResponse>) {
                if (response.isSuccessful) {
                    val res = response.body()!!
                    Log.d("__res", response.body()!!.toString())
                    when (res.code) {
                        1000 -> { //성공
                            Log.d("CardService-patchTitle", res.code.toString() + " : " + res.message)
                            serverView.onServerSuccess()
                        }
                        else -> { //의도된 실패
                            Log.d("CardService-patchTitle", res.code.toString() + " : " + res.message)
                            serverView.onServerFailure(res.code, res.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ServerDefaultResponse>, t: Throwable) {
                serverView.onServerFailure(400, t.message.toString())
                Log.d("CardService-patchTitle", t.toString()) //네트워크 실패
            }
        })
    }

    /**
     * PersonalSchedule 전송
     */
    fun postPersonalSchedule( schedule: String ){
        Log.d("CheckPoint : ", "CardService-postCard Activated")
        postPersonalScheduleView.onPostPersonalScheduleLoading()

        val userIdx = getUserIdx()

        val cardRetrofitService = retrofit.create(RetrofitInterface::class.java)
        cardRetrofitService.postPersonalSchedule(userIdx, schedule).enqueue(object : Callback<PostPersonalScheduleResponse> {
            override fun onResponse(call: Call<PostPersonalScheduleResponse>, response: Response<PostPersonalScheduleResponse>){
                if (response.isSuccessful) {
                    val res = response.body()!!
                    Log.d("__res", response.body()!!.toString())
                    when (res.code) {
                        1000 -> { //성공
                            Log.d("CardService-postCard", res.code.toString() + " : " + res.message+ "courseIdx : "+ res.result)
                            postPersonalScheduleView.onPostPersonalScheduleSuccess(res.result)
                        }
                        else -> { //의도된 실패
                            Log.d("CardService-postCard", res.code.toString() + " : " + res.message)
                            postPersonalScheduleView.onPostPersonalScheduleFailure(res.code, res.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<PostPersonalScheduleResponse>, t: Throwable) {
                postPersonalScheduleView.onPostPersonalScheduleFailure(400, t.message.toString())
                Log.d("CardService-postCard", t.toString()) //네트워크 실패
            }
        })
    }

    /**
     * 내가 소속된 모임들 가져오기
     */
    fun getMoims(){
        val retrofitService = retrofit.create(RetrofitInterface::class.java)

        val userIdx = getUserIdx()
        getMoimsView.onGetMoimsLoading()

        retrofitService.getMoims(userIdx).enqueue(object : Callback<GetMoimsResponse> {
            override fun onResponse(call: Call<GetMoimsResponse>, response: Response<GetMoimsResponse>) {
                Log.d("들어오는지 확인", "Retrofit-getMoims")
                if (response.isSuccessful) {
                    val res = response.body()!!
                    Log.d("__res", response.body()!!.toString())
                    when (res.code) {
                        1000 -> {
                            Log.d("Retrofit-getMoims", res.toString())
                            getMoimsView.onGetMoimsSuccess(res.result)
                        }

                        else -> {
                            Log.d("Retrofit-getMoims", res.toString())
                            getMoimsView.onGetMoimsFailure(res.code, res.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<GetMoimsResponse>, t: Throwable) {
                Log.d("들어오는지 확인", "CardService-getTrip-onFailure")
                postMoimScheduleView.onPostMoimScheduleFailure(400, t.message.toString())
            }
        })
    }

    /**
     *  가져오기
     */
    fun deleteMoim(moimIdx: Int) {
        Log.d("CheckPoint : ", "CardService-deleteTrip Activated")

        val userIdx = getUserIdx()

        serverView.onServerLoading()

        val retrofitService = retrofit.create(RetrofitInterface::class.java)
        retrofitService.deleteMoim(userIdx, moimIdx).enqueue(object : Callback<ServerDefaultResponse> {
            override fun onResponse(call: Call<ServerDefaultResponse>, response: Response<ServerDefaultResponse>) {
                if (response.isSuccessful) {
                    val res = response.body()!!
                    Log.d("__res", response.body()!!.toString())
                    when (res.code) {
                        1000 -> { //성공
                            Log.d("CardService-deleteTrip", res.code.toString() + " : " + res.message)
                            serverView.onServerSuccess()
                        }
                        else -> { //의도된 실패
                            Log.d("CardService-deleteTrip", res.code.toString() + " : " + res.message)
                            serverView.onServerFailure(res.code, res.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ServerDefaultResponse>, t: Throwable) {
                serverView.onServerFailure(400, t.message.toString())
                Log.d("CardService-deleteTrip", t.toString()) //네트워크 실패
            }
        })
    }

    fun deleteCard(courseIdx : String) {
        Log.d("CheckPoint : ", "CardService-deleteCard Activated")

        val userIdx = getUserIdx()

        serverView.onServerLoading()

        val cardRetrofitService = retrofit.create(RetrofitInterface::class.java)
        cardRetrofitService.deleteCard(userIdx, courseIdx).enqueue(object : Callback<ServerDefaultResponse> {
            override fun onResponse(call: Call<ServerDefaultResponse>, response: Response<ServerDefaultResponse>) {
                if (response.isSuccessful) {
                    val res = response.body()!!
                    Log.d("__res", response.body()!!.toString())
                    when (res.code) {
                        1000 -> { //성공
                            Log.d("CardService-deleteCard", res.code.toString() + " : " + res.message)
                            serverView.onServerSuccess()
                        }
                        else -> { //의도된 실패
                            Log.d("CardService-deleteCard", res.code.toString() + " : " + res.message)
                            serverView.onServerFailure(res.code, res.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ServerDefaultResponse>, t: Throwable) {
                serverView.onServerFailure(400, t.message.toString())
                Log.d("CardService-patchTitle", t.toString()) //네트워크 실패
            }
        })
    }


}
