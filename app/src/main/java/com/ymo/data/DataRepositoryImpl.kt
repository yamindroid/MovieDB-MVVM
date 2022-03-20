package com.ymo.data

import com.ymo.data.local.db.DBHelper
import com.ymo.data.local.prefs.PreferencesHelper
import com.ymo.data.model.api.GenreResponse
import com.ymo.data.model.api.GenresItem
import com.ymo.data.model.api.MovieDetail
import com.ymo.data.model.api.MovieResponse
import com.ymo.data.model.db.FavoriteMovie
import com.ymo.data.remote.ApiHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val apiHelper: ApiHelper,
    private val preferencesHelper: PreferencesHelper,
    private val dbHelper: DBHelper
) : DataRepositoryHelper {

    override suspend fun loadNowPlayingMovies(page: Int): MovieResponse {
        loadGenresAndSaveToDB()
        return apiHelper.loadNowPlayingMovies(page)
    }

    override suspend fun loadPopularMovies(page: Int): MovieResponse {
        loadGenresAndSaveToDB()
        return apiHelper.loadPopularMovies(page)
    }

    override suspend fun loadTopRatedMovies(page: Int): MovieResponse {
        loadGenresAndSaveToDB()
        return apiHelper.loadTopRatedMovies(page)
    }

    override suspend fun loadUpComingMovies(page: Int): MovieResponse {
        loadGenresAndSaveToDB()
        return apiHelper.loadUpComingMovies(page)
    }

    override suspend fun loadMovieDetails(movieId: Int): MovieDetail =
        apiHelper.loadMovieDetails(movieId)

    override suspend fun searchMoviesByQuery(query: String, page: Int): MovieResponse {
        loadGenresAndSaveToDB()
        return apiHelper.searchMoviesByQuery(query, page)
    }


    override suspend fun loadGenres(): GenreResponse =
        apiHelper.loadGenres()


    override suspend fun getFavoriteMovies(): List<FavoriteMovie> =
        dbHelper.getFavoriteMovies()

    override suspend fun addFavoriteMovie(favoriteMovie: FavoriteMovie) =
        dbHelper.addFavoriteMovie(favoriteMovie)

    override suspend fun deleteFavoriteMovieById(id: Int) =
        dbHelper.deleteFavoriteMovieById(id)

    override suspend fun getGenres(): List<GenresItem> =
        dbHelper.getGenres()

    override suspend fun addGenre(genresItem: GenresItem) =
        dbHelper.addGenre(genresItem)

    override suspend fun loadGenresAndSaveToDB() {
        withContext(Dispatchers.IO) {
            val response = apiHelper.loadGenres()
            val apiGenres = response.genres
            apiGenres?.map { genre ->
                genre?.let {
                    dbHelper.addGenre(it)
                }
            }
        }
    }

    override fun setToken(token: String) = preferencesHelper.setToken(token)

    override fun getToken(): String = preferencesHelper.getToken()
}
