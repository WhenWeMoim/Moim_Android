package com.legends.moim.src.main

import android.annotation.SuppressLint
import android.os.Bundle
import com.legends.moim.config.BaseActivity
import com.legends.moim.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
    }
}