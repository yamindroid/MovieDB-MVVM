package com.ymo.data.model.db

import androidx.room.*
import com.ymo.data.local.db.favorites.GenreTypeConverter
import com.ymo.data.model.api.GenresItem

@Entity(tableName = "favorite")
data class FavoriteMovie @JvmOverloads constructor(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "poster_path") val posterPath: String? = null,
    @ColumnInfo(name = "release_date") val releaseDate: String? = null,
    @ColumnInfo(name = "title") val title: String? = null,
    @ColumnInfo(name = "vote_average") val voteAverage: Double? = null,
    @ColumnInfo(name = "vote_count") val voteCount: Int? = null,
    @ColumnInfo(name = "genre_ids")
    @TypeConverters(GenreTypeConverter::class)
    val genres: List<GenresItem?>? = null
)


