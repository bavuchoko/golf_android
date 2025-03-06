package com.bavuchoko.jsparkgolf.common

import android.content.Context
import android.content.SharedPreferences
import com.auth0.android.jwt.JWT
import com.bavuchoko.jsparkgolf.vo.Sido

object CommonMethod {

    val PREFERENCES_ALIAS: String = "golfPreferences";


    fun saveAccessToken(context : Context, token : String)  {
        val preference : SharedPreferences = context.getSharedPreferences(PREFERENCES_ALIAS, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = preference.edit()
        editor.putString("accessToken", token);
        editor.apply();
    }


    fun saveValue(context : Context, target: String, token : String)  {
        val preference : SharedPreferences = context.getSharedPreferences(PREFERENCES_ALIAS, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = preference.edit()
        editor.putString(target, token);
        editor.apply();
    }

    fun getToken(context: Context): String? {
        val preference : SharedPreferences = context.getSharedPreferences(PREFERENCES_ALIAS, Context.MODE_PRIVATE)
        return preference.getString("accessToken", null);
    }


    fun getValue(context: Context, type: String): String?{
        val jwtToken = getToken(context)
        return jwtToken?.let {
            try {
                val jwt = JWT(it) // 안전 호출로 null 확인 후 처리
                jwt.getClaim(type).asString()
            } catch (e: Exception) {
                e.printStackTrace()
                null // 예외 발생 시 null 반환
            }
        }
    }
    fun getStoredValue(context: Context, type: String): String?{
        val preference: SharedPreferences = context.getSharedPreferences(PREFERENCES_ALIAS, Context.MODE_PRIVATE)
        return preference.getString(type, null)
    }


    fun clearAllValue(context: Context) {
        val preference : SharedPreferences = context.getSharedPreferences(PREFERENCES_ALIAS, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preference.edit()
        editor.clear()
        editor.apply()
    }


    fun clearValue(context: Context, key: String) {
        val preference : SharedPreferences = context.getSharedPreferences(PREFERENCES_ALIAS, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preference.edit()
        editor.remove(key)
        editor.apply()
    }

    fun getRegionCode(context: Context): String? {
        val preference: SharedPreferences = context.getSharedPreferences(PREFERENCES_ALIAS, Context.MODE_PRIVATE)
        val region = preference.getString("region", null)
        return region?.let { Sido.getNumberByName(region) }
    }
}