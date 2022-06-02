package com.legends.moim.utils.retrofit

import android.util.Log
import com.legends.moim.utils.ApplicationClass.Companion.retrofit
import com.legends.moim.utils.getUserIdx
import retrofit2.*

class RetrofitService{

    private lateinit var serverView: ServerView
    fun setServerView(serverView: ServerView) {
        this.serverView = serverView
    }

    private lateinit var postPersonalScheduleView : PostPersonalScheduleView
    fun setCardsView(postPersonalScheduleView : PostPersonalScheduleView) {
        this.postPersonalScheduleView = postPersonalScheduleView
    }

    private lateinit var postGroupScheduleView : PostGroupScheduleView
    fun setCardsView(postGroupScheduleView : PostGroupScheduleView) {
        this.postGroupScheduleView = postGroupScheduleView
    }

    /**
     * PersonalSchedule 서버로 전송
     */
    fun postPersonalSchedule(schedule: String){
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
     * 서버에서 GroupSchedule 정보 가져오기
     */
    fun getGroupSchedule(tripIdx : String){
        val cardRetrofitService = retrofit.create(RetrofitInterface::class.java)

        val userIdx = getUserIdx()
        postGroupScheduleView.onPostGroupScheduleLoading()

        cardRetrofitService.getGroupSchedule(userIdx, tripIdx).enqueue(object : Callback<GetGroupScheduleResponse> {
            override fun onResponse(call: Call<GetGroupScheduleResponse>, response: Response<GetGroupScheduleResponse>) {
                Log.d("들어오는지 확인", "CardService-getTrip-onResponse")
                if (response.isSuccessful) {
                    val res = response.body()!!
                    Log.d("__res", response.body()!!.toString())
                    when (res.code) {
                        1000 -> {
                            Log.d("REST API TEST 성공", res.toString())
                            postGroupScheduleView.onPostGroupScheduleSuccess(res.result)
                        }

                        else -> {
                            Log.d("통신 실패 : ", res.toString())
                            postGroupScheduleView.onPostGroupScheduleFailure(res.code, res.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<GetGroupScheduleResponse>, t: Throwable) {
                Log.d("들어오는지 확인", "CardService-getTrip-onFailure")
                postGroupScheduleView.onPostGroupScheduleFailure(400, t.message.toString())
            }
        })
    }

    /**
     * 서버에서 모임 삭제
     */
    fun deleteMoim(moimIdx: Int) {
        Log.d("CheckPoint : ", "CardService-deleteTrip Activated")

        val userIdx = getUserIdx()

        serverView.onServerLoading()

        val cardRetrofitService = retrofit.create(RetrofitInterface::class.java)
        cardRetrofitService.deleteMoim(userIdx, moimIdx).enqueue(object : Callback<ServerDefaultResponse> {
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
}
