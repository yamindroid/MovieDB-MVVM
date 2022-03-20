package com.ymo.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ymo.data.model.db.FavoriteMovie
import com.ymo.data.local.db.favorites.FavoriteMovieDao
import com.ymo.data.local.db.favorites.GenreTypeConverter
import com.ymo.data.local.db.genres.GenreDao
import com.ymo.data.model.api.GenresItem

@Database(entities = [FavoriteMovie::class,GenresItem::class], version = 1)
@TypeConverters(GenreTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteMovieDao(): FavoriteMovieDao
    abstract fun genreDao():GenreDao
}