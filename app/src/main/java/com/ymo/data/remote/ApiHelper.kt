package com.ymo.data.remote

import com.ymo.data.model.api.GenreResponse
import com.ymo.data.model.api.GenresItem
import com.ymo.data.model.api.MovieDetail
import com.ymo.data.model.api.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiHelper {
    suspend fun loadNowPlayingMovies(page: Int): MovieResponse
    suspend fun loadPopularMovies(page: Int): MovieResponse
    suspend fun loadTopRatedMovies(page: Int): MovieResponse
    suspend fun loadUpComingMovies(page: Int): MovieResponse
    suspend fun loadMovieDetails(movieId: Int): MovieDetail
    suspend fun searchMoviesByQuery(query: String, page: Int): MovieResponse
    suspend fun loadGenres(): GenreResponse
}
