package com.ymo.data

import com.ymo.data.local.db.DBHelper
import com.ymo.data.remote.ApiHelper

interface DataRepositoryHelper : ApiHelper, DBHelper{
    suspend fun loadGenresAndSaveToDB():Unit
}