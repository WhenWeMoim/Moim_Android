package com.legends.moim.src.main

import android.content.Intent
import android.os.Bundle
import com.legends.moim.config.BaseActivity
import com.legends.moim.R
import com.kakao.sdk.user.UserApiClient
import android.widget.Toast
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause.*
import android.widget.ImageButton
import android.util.Log
import com.legends.moim.utils.retrofit.LoginView
import com.legends.moim.utils.retrofit.RetrofitService
import com.legends.moim.utils.saveNickname
import com.legends.moim.utils.saveUserIdx

private const val TAG = "LoginActivity"

class LoginActivity: BaseActivity(), LoginView {

    private val retrofitService = RetrofitService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        retrofitService.setLoginView(this)

        val loginBtn = findViewById<ImageButton>(R.id.kakao_login_button) // 로그인 버튼

        //dummy Function todo change to checkKakaoLoginInfo()
        loginBtn.setOnClickListener {
            loginDummy("박재형", "11111")
            //signinByKakaotalk() todo 카카오 로그인으로 수정
        }

        // 키해시 값 찾기
        //var keyHash = Utility.getKeyHash(this)
        //Log.e("getkeyHasg", ""+keyHash)

        // 로그인 정보 확인
        // todo test 끝
        //checkKakaoLoginInfo(loginBtn)
    }

    private fun loginDummy(userName: String, userEmail: String) {
        retrofitService.postLogin(userName = userName, userEmail = userEmail)
        saveNickname(userName)
    }

    private fun signinByKakaotalk() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("LoginActivity", error.toString())
            } else if (token != null) {
                Log.d("LoginActivity", "signinByKakaoTalk()")
                UserApiClient.instance.me { user, _ ->
                    if (user!!.kakaoAccount?.emailNeedsAgreement == true) {
                        requireEmailNeedsAgreement()
                    } else {
                        val userName = user.kakaoAccount!!.profile!!.nickname!!
                        val userEmail = user.kakaoAccount!!.email!!
                        saveNickname(userName)
//                        saveKakaoAccessToken(token.accessToken)
//                        saveKakaoRefreshToken(token.refreshToken)

//                        val tokens = HashMap<String, String>()
//                        tokens["kakaoAccessToken"] = token.accessToken
//                        tokens["kakaoRefreshToken"] = token.refreshToken
                        retrofitService.postLogin(userName = userName, userEmail = userEmail)
                    }
                }
            }
        }
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)) {
            UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this@LoginActivity, callback = callback)
        }
    }

    private fun requireEmailNeedsAgreement() {
        val scopes = mutableListOf<String>()

        scopes.add("account_email")
        Log.d(TAG, "사용자에게 추가 동의를 받아야 합니다.")

        UserApiClient.instance.loginWithNewScopes(this@LoginActivity, scopes) { token, error ->
            if (error != null) {
                Log.e(TAG, "사용자 추가 동의 실패", error)
                Toast.makeText(this, "이메일 사용에 동의해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                Log.d(TAG, "allowed scopes: ${token!!.scopes}")
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e(TAG, "사용자 정보 요청 실패", error)
                    } else if (user != null) {
                        val userName = user.kakaoAccount!!.profile!!.nickname!!
                        val userEmail = user.kakaoAccount!!.email!!
                        saveNickname(userName)
//                        saveKakaoAccessToken(token.accessToken)
//                        saveKakaoRefreshToken(token.refreshToken)

//                        val tokens = HashMap<String, String>()
//                        tokens["kakaoAccessToken"] = token.accessToken
//                        tokens["kakaoRefreshToken"] = token.refreshToken
                        retrofitService.postLogin(userName = userName, userEmail = userEmail)
                    }
                }
            }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onLoginLoading() {
        Log.d("LoginActivity", "LoginLoading")
    }

    override fun onLoginSuccess(result: Int) {
        //Toast.makeText(this, "userIdx : $result", Toast.LENGTH_SHORT).show()
        saveUserIdx( result )
        startMainActivity()
    }

    override fun onLoginFailure(code: Int, message: String) {
        Toast.makeText(this, "$code 로그인 실패 : $message", Toast.LENGTH_LONG).show()
    }

    /*---기존상태---*/

    private fun checkKakaoLoginInfo(kakao_login_button: ImageButton) {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
            } else if (tokenInfo != null) {
                Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "로그인 실패", error)

                when {
                    error.toString() == AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT)
                            .show()
                    }
                    error.toString() == InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT)
                            .show()
                    }
                    error.toString() == ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (token != null) {
                Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }


        kakao_login_button.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)) {
                UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity, callback = callback)


            } else {
                Toast.makeText(this, "로그인하러 이동중.", Toast.LENGTH_SHORT).show()
                UserApiClient.instance.loginWithKakaoAccount(
                    this@LoginActivity,
                    callback = callback
                )
            }
        }
    }
}