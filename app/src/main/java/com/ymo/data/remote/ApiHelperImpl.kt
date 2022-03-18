package com.ymo.data.remote

import com.ymo.data.model.api.MovieDetail
import com.ymo.data.model.api.MovieResponse
import com.ymo.data.remote.service.ApiService
import javax.inject.Inject

class ApiHelperImpl @Inject
constructor(private val apiService: ApiService) : ApiHelper {

    override suspend fun loadUpComingMovies(): MovieResponse =
        apiService.loadUpComingMovies(com.ymo.BuildConfig.API_KEY)

    override suspend fun loadTopRatedMovies(): MovieResponse =
        apiService.loadPopularMovies(com.ymo.BuildConfig.API_KEY)

    override suspend fun loadMovieDetails(movieId: Int): MovieDetail =
        apiService.loadMovieDetails(movieId, com.ymo.BuildConfig.API_KEY)

    override suspend fun searchMoviesByQuery(query: String, page: Int): MovieResponse =
        apiService.searchMoviesByQuery(query, page, com.ymo.BuildConfig.API_KEY)


}
