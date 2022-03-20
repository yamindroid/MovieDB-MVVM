package com.ymo.data.local.db.favorites

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ymo.data.model.api.GenresItem

class GenreTypeConverter {

    @TypeConverter
    fun saveGenreList(list: List<GenresItem?>?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun getGenreList(list: String): List<GenresItem?>? {
        return Gson().fromJson(
            list.toString(),
            object : TypeToken<List<GenresItem?>?>() {}.type
        )
    }
}