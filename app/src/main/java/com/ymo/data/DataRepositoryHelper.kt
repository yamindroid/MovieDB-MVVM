package com.ymo.data

import com.ymo.data.local.db.DBHelper
import com.ymo.data.local.prefs.PreferencesHelper
import com.ymo.data.model.api.MovieItem
import com.ymo.data.remote.ApiHelper

interface DataRepositoryHelper : ApiHelper, PreferencesHelper, DBHelper{
    suspend fun loadGenresAndSaveToDB():Unit
}