package com.ymo.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ymo.data.model.db.FavoriteMovie
import com.ymo.data.local.db.favorites.FavoriteMovieDao

@Database(entities = [FavoriteMovie::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteMovieDao(): FavoriteMovieDao
}