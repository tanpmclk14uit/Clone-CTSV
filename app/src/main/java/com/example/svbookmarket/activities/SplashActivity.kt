package com.example.svbookmarket.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.data.FullBookList

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        FullBookList.getInstance().lstFullBook
        // set full screen
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        // set time to pass this activity
        @Suppress("DEPRECATION")
        Handler().postDelayed(
                {
                    startActivity(Intent(baseContext,WelcomeActivity::class.java))
                    finish()
                },
                3000 // set time
        )
    }
}