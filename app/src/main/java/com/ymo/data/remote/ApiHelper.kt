package com.ymo.data.remote

import com.ymo.data.model.api.MovieDetail
import com.ymo.data.model.api.MovieResponse

interface ApiHelper {
    suspend fun loadUpComingMovies(): MovieResponse
    suspend fun loadTopRatedMovies(): MovieResponse
    suspend fun loadMovieDetails(movieId: Int): MovieDetail
}
