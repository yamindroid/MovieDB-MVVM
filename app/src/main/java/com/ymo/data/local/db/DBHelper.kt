package com.ymo.data.local.db

import com.ymo.data.model.db.FavoriteMovie


interface DBHelper {
    suspend fun getFavoriteMovies(): List<FavoriteMovie>
    suspend fun addFavoriteMovie(favoriteMovie: FavoriteMovie)
    suspend fun deleteFavoriteMovieById(id: Int): Int
}