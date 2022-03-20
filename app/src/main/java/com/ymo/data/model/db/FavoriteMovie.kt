package com.ymo.data.model.db

import androidx.room.*
import com.ymo.data.local.db.favorites.GenreTypeConverter
import com.ymo.data.model.api.GenresItem

@Entity(tableName = "favorite")
data class FavoriteMovie @JvmOverloads constructor(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "poster_path") val posterPath: String,
    @ColumnInfo(name = "release_date") val releaseDate: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "vote_average") val voteAverage: Double,
    @ColumnInfo(name = "vote_count") val voteCount: Int,
    @ColumnInfo(name = "genre_ids")
    @TypeConverters(GenreTypeConverter::class)
    val genres: List<GenresItem?>?
)


