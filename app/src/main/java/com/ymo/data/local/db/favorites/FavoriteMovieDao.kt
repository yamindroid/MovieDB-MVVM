package com.ymo.data.local.db.favorites

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ymo.data.model.db.FavoriteMovie

@Dao
interface FavoriteMovieDao {

    @Query("SELECT * FROM favorite")
    suspend fun getFavoriteMovies(): List<FavoriteMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteMovie(favoriteMovie: FavoriteMovie)

    @Query("DELETE FROM favorite WHERE id = :id")
    suspend fun deleteFavoriteMovieById(id: Int): Int

}