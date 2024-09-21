package com.bavuchoko.jsparkgolf.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.ui.login.LoginActivity
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        val sharedPref = getSharedPreferences("golfPreferences", Context.MODE_PRIVATE)
        val accessToken = sharedPref.getString("accessToken", null)

        if (accessToken == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish() // MainActivity 종료
        }

        // 로그인한 경우에 대한 다른 초기화 작업
    }


    override fun onResume() {
        super.onResume()

        val sharedPref = getSharedPreferences("golfPreferences", Context.MODE_PRIVATE)
        val accessToken = sharedPref.getString("accessToken", null)

        if (accessToken != null) {

        }
    }
}

