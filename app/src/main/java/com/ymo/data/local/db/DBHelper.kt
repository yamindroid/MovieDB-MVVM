package com.ymo.data.local.db

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ymo.data.model.api.GenresItem
import com.ymo.data.model.db.FavoriteMovie


interface DBHelper {
    suspend fun getFavoriteMovies(): List<FavoriteMovie>
    suspend fun addFavoriteMovie(favoriteMovie: FavoriteMovie)
    suspend fun deleteFavoriteMovieById(id: Int): Int
    suspend fun getGenres(): List<GenresItem>
    suspend fun addGenre(genresItem: GenresItem)
}