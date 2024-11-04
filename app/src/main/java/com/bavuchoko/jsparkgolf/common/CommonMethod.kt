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
        try {
            val jwt = JWT(token)
            editor.putString("name", jwt.getClaim("name").asString())
            editor.putString("city", jwt.getClaim("city").asString()?: "??")
            editor.putString("age", jwt.getClaim("age").asString() ?: "??")
            editor.putString("gender", jwt.getClaim("gender").asString())
            editor.putString("birth", jwt.getClaim("birth").asString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        editor.apply();
    }


    fun saveValue(context : Context, target: String, token : String)  {
        val preference : SharedPreferences = context.getSharedPreferences(PREFERENCES_ALIAS, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = preference.edit()
        editor.putString("target", token);
        editor.apply();
    }

    fun getToken(context: Context): String? {
        val preference : SharedPreferences = context.getSharedPreferences(PREFERENCES_ALIAS, Context.MODE_PRIVATE)
        return preference.getString("accessToken", null);
    }


    fun getValue(context: Context, type: String): String{
        val jwtToken = getToken(context)
        if (jwtToken != null) {
            try {
                val jwt = JWT(jwtToken)
                val value = jwt.getClaim(type).asString()
                return value ?: "Unknown"
            } catch (e: Exception) {
                e.printStackTrace()
                return "Error parsing token"
            }
        } else {
            return "Token not found"
        }
    }


    fun clearValue(context: Context, key: String) {
        val preference : SharedPreferences = context.getSharedPreferences(PREFERENCES_ALIAS, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preference.edit()
        editor.remove(key)
        editor.apply()
    }
}