package com.ymo.data.remote

import com.ymo.data.model.api.MovieDetail
import com.ymo.data.model.api.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiHelper {
    suspend fun loadUpComingMovies(): MovieResponse
    suspend fun loadTopRatedMovies(): MovieResponse
    suspend fun loadMovieDetails(movieId: Int): MovieDetail
    suspend fun searchMoviesByQuery(query: String, page: Int): MovieResponse
}
