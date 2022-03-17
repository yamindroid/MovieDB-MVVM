package com.ymo.data.local.prefs

import android.content.SharedPreferences
import javax.inject.Inject

class AppPreferencesImpl @Inject constructor(private val pref: SharedPreferences) : PreferencesHelper {
    override  fun getToken(): String = pref.getString(PREF_KEY_ACCESS_TOKEN, "").toString()

    override  fun setToken(token: String) {
        pref.edit().putString(PREF_KEY_ACCESS_TOKEN, token).apply();
    }

    companion object {
        private const val PREF_KEY_ACCESS_TOKEN = "access_token"
    }

}