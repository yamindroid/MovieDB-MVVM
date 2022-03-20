package com.ymo.data.local.db

import com.ymo.data.model.api.GenresItem
import com.ymo.data.model.db.FavoriteMovie
import javax.inject.Inject

class DBHelperImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : DBHelper {
    override suspend fun getFavoriteMovies() = appDatabase.favoriteMovieDao().getFavoriteMovies()

    override suspend fun addFavoriteMovie(favoriteMovie: FavoriteMovie) =
        appDatabase.favoriteMovieDao().addFavoriteMovie(favoriteMovie)

    override suspend fun deleteFavoriteMovieById(id: Int) =
        appDatabase.favoriteMovieDao().deleteFavoriteMovieById(id)

    override suspend fun getGenres(): List<GenresItem> =
       appDatabase.genreDao().getGenres()

    override suspend fun addGenre(genresItem: GenresItem) =
        appDatabase.genreDao().addGenre(genresItem)

}
