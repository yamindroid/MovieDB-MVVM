package com.ymo.data.local.prefs

interface PreferencesHelper {
   fun getToken():String
   fun setToken(token:String)
}