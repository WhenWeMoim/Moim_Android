package com.legends.moim.src.main
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.legends.moim.R
import com.legends.moim.utils.getUserIdx

@SuppressLint("CustomSplashScreen")
class SplashActivity: AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        selectActivity()

        //Handler(Looper.getMainLooper()).postDelayed({
            //액티비티 이동

//            startLoginActivity()
        //},DURATION)
    }
    companion object {
        private const val DURATION : Long = 1500
    }

    private fun selectActivity() {
        Log.d("SplashActivity.kt", " \nselectActivity()" +
                "\nuserIdx: " + getUserIdx()
//                "\nAT: " + getAccessToken() +
//                "\nRT: " + getRefreshToken() +
//                "\nKAT: " + getKakaoAccessToken() +
//                "\nKRT: " + getKakaoRefreshToken()
        )
        val userIdx = getUserIdx()
        if (userIdx == -1) { //로그인이 안된상태
            startLoginActivity()
        } else { //로그인이 된 상태
            startMainActivity()
        }
    }

    private fun startLoginActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }, DURATION)
    }

    private fun startMainActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }, DURATION)
    }
}