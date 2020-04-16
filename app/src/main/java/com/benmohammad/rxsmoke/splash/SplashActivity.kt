package com.benmohammad.rxsmoke.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.benmohammad.rxsmoke.AppPreferences
import com.benmohammad.rxsmoke.auth.LoginActivity
import com.benmohammad.rxsmoke.base.BaseActivity
import com.benmohammad.rxsmoke.home.HomeActivity
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        routeTo()
    }

    private fun routeTo() {
        Handler().postDelayed({
            if(appPreferences.isLoggedIn) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 1000)

    }}