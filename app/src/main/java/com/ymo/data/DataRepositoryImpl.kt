package com.ymo.data

import com.ymo.data.local.db.DBHelper
import com.ymo.data.local.prefs.PreferencesHelper
import com.ymo.data.model.api.MovieDetail
import com.ymo.data.model.api.MovieResponse
import com.ymo.data.model.db.FavoriteMovie
import com.ymo.data.remote.ApiHelper
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val apiHelper: ApiHelper,
    private val preferencesHelper: PreferencesHelper,
    private val dbHelper: DBHelper
) : DataRepositoryHelper {

    override suspend fun loadNowPlayingMovies(page: Int): MovieResponse =
        apiHelper.loadNowPlayingMovies(page)

    override suspend fun loadPopularMovies(page: Int): MovieResponse =
        apiHelper.loadPopularMovies(page)

    override suspend fun loadTopRatedMovies(page: Int): MovieResponse =
        apiHelper.loadTopRatedMovies(page)

    override suspend fun loadUpComingMovies(page: Int): MovieResponse =
        apiHelper.loadUpComingMovies(page)

    override suspend fun loadMovieDetails(movieId: Int): MovieDetail =
        apiHelper.loadMovieDetails(movieId)

    override suspend fun getFavoriteMovies(): List<FavoriteMovie> =
        dbHelper.getFavoriteMovies()

    override suspend fun addFavoriteMovie(favoriteMovie: FavoriteMovie) =
        dbHelper.addFavoriteMovie(favoriteMovie)

    override suspend fun deleteFavoriteMovieById(id: Int) =
        dbHelper.deleteFavoriteMovieById(id)

    override suspend fun searchMoviesByQuery(query: String, page: Int): MovieResponse =
        apiHelper.searchMoviesByQuery(query, page)

    override fun setToken(token: String) = preferencesHelper.setToken(token)

    override fun getToken(): String = preferencesHelper.getToken()
}
