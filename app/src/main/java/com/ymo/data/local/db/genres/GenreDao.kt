package com.ymo.data.local.db.genres

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ymo.data.model.api.GenresItem
import com.ymo.data.model.api.MovieItem
import com.ymo.data.model.db.FavoriteMovie

@Dao
interface GenreDao {

    @Query("SELECT * FROM genre")
    suspend fun getGenres(): List<GenresItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGenre(genresItem: GenresItem)

}