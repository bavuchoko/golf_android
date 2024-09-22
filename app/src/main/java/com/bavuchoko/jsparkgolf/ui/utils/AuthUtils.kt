package com.bavuchoko.jsparkgolf.ui.utils

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.bavuchoko.jsparkgolf.ui.login.LoginActivity

object AuthUtils {
    fun checkLoginStatus(activity: AppCompatActivity) {
        val sharedPref = activity.getSharedPreferences("golfPreferences", Context.MODE_PRIVATE)
        val accessToken = sharedPref.getString("accessToken", null)

        if (accessToken == null) {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(intent)
            activity.finish()
        }
    }
}