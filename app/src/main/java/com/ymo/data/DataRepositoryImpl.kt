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

    override suspend fun loadUpComingMovies(): MovieResponse = apiHelper.loadUpComingMovies()
    override suspend fun loadTopRatedMovies(): MovieResponse = apiHelper.loadTopRatedMovies()
    override suspend fun loadMovieDetails(movieId: Int): MovieDetail = apiHelper.loadMovieDetails(movieId)

    override suspend fun getFavoriteMovies(): List<FavoriteMovie> = dbHelper.getFavoriteMovies()
    override suspend fun addFavoriteMovie(favoriteMovie: FavoriteMovie) = dbHelper.addFavoriteMovie(favoriteMovie)
    override suspend fun deleteFavoriteMovieById(id: Int) = dbHelper.deleteFavoriteMovieById(id)

    override fun setToken(token: String) = preferencesHelper.setToken(token)
    override fun getToken(): String = preferencesHelper.getToken()
}
