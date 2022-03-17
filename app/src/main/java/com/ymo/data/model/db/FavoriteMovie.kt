package com.ymo.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class FavoriteMovie @JvmOverloads constructor(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "poster_path") val posterPath: String,
    @ColumnInfo(name = "release_date") val releaseDate: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "vote_average") val voteAverage: Double,
    @ColumnInfo(name = "vote_count") val voteCount: Int,
)
