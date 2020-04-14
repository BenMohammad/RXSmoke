package com.benmohammad.rxsmoke.auth

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.widget.Button
import android.widget.ProgressBar
import com.benmohammad.rxsmoke.AppPreferences
import com.benmohammad.rxsmoke.HomeActivity
import com.benmohammad.rxsmoke.R
import com.benmohammad.rxsmoke.base.BaseActivity
import com.benmohammad.rxsmoke.constants.AppConstants
import javax.inject.Inject

class LoginActivity : BaseActivity() {

    @Inject
    lateinit var appPreferences: AppPreferences

    private var progressBar: ProgressBar? = null
    private var buttonLogin: Button? = null
    private var buttonSkip: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        buttonLogin = findViewById(R.id.button_auth)
        buttonSkip = findViewById(R.id.button_skip_login)
        progressBar = findViewById(R.id.progress_loading)
        progressBar?.visibility = View.GONE
        buttonLogin?.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW,
            Uri.parse(AppConstants.AUTH_URL + "?" + AppConstants.CLIENT_ID + "-" + getString(R.string.client_id) +
            "&" + AppConstants.REDIRECT_URI + "-" + getString(R.string.redirect_uri)))
            startActivity(intent)
            showProgress()
        }
        buttonSkip?.setOnClickListener { routeToHome()}
        checkLoggedOutintent()
    }

    private fun checkLoggedOutintent() {
        val isJustLoggedOut = intent?.extras?.getBoolean(AppConstants.IS_JUST_LOGGED_OUT)
        when(isJustLoggedOut) {
            true -> clearCookies()
        }
    }

    private fun clearCookies() {
        CookieSyncManager.createInstance(this)
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookie()
    }

    override fun onResume() {
        super.onResume()
        handleIntent()
    }

    private fun handleIntent() {
        val uri = intent.data
        if(uri != null && uri.toString().startsWith(getString(R.string.redirect_uri))) {
            val extra = uri.fragment
            val accessToken = extra?.split("&")?.get(0)?.split("=")?.get(1)
            if(accessToken != null) {
                appPreferences.setIsLoggedIn(true)
                appPreferences.accessToken = accessToken
                routeToHome()
            } else if(uri.getQueryParameter(AppConstants.ERROR) != null) {
                hideProgress()
                val error = uri.getQueryParameter(AppConstants.ERROR)
                Log.e("Ben Mohammad", error)
            }
        } else {
            hideProgress()
        }
    }

    private fun showProgress() {
        progressBar?.visibility = View.VISIBLE
        buttonLogin?.visibility = View.GONE
    }

    private fun hideProgress() {
        progressBar?.visibility = View.GONE
        buttonLogin?.visibility = View.VISIBLE
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("Ben Mohammad", intent.toString())
    }

    private fun routeToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}