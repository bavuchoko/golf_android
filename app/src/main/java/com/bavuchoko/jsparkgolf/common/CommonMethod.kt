package com.bavuchoko.jsparkgolf.common

import android.content.Context
import android.content.SharedPreferences
import com.auth0.android.jwt.JWT

object CommonMethod {

    val PREFERENCES_ALIAS: String = "golfPreferences";


    fun saveAccessToken(context : Context, token : String)  {
        val preference : SharedPreferences = context.getSharedPreferences(PREFERENCES_ALIAS, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = preference.edit()
        editor.putString("accessToken", token);
        editor.apply();
    }

    fun getToken(context: Context): String? {
        val preference : SharedPreferences = context.getSharedPreferences(PREFERENCES_ALIAS, Context.MODE_PRIVATE)
        return preference.getString("accessToken", null);
    }


    fun getName(jwtToken: String): String{
        try {
            val jwt = JWT(jwtToken)

            val userName = jwt.getClaim("name").asString()
            return userName ?: "Unknown User"
        } catch (e: Exception) {
            e.printStackTrace()
            return "Error parsing token"
        }
    }

    fun clearValue(context: Context, key: String) {
        val preference : SharedPreferences = context.getSharedPreferences(PREFERENCES_ALIAS, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preference.edit()
        editor.remove(key)
        editor.apply()
    }
}