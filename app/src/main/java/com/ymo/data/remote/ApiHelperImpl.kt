package com.ymo.data.remote

import com.ymo.BuildConfig
import com.ymo.data.model.api.GenreResponse
import com.ymo.data.model.api.MovieDetail
import com.ymo.data.model.api.MovieResponse
import com.ymo.data.remote.service.ApiService
import javax.inject.Inject

class ApiHelperImpl @Inject
constructor(private val apiService: ApiService) : ApiHelper {

    override suspend fun loadNowPlayingMovies(page: Int): MovieResponse =
        apiService.loadNowPlayingMovies(page, BuildConfig.API_KEY)

    override suspend fun loadPopularMovies(page: Int): MovieResponse =
        apiService.loadPopularMovies(page, BuildConfig.API_KEY)

    override suspend fun loadTopRatedMovies(page: Int): MovieResponse =
        apiService.loadTopRatedMovies(page, BuildConfig.API_KEY)

    override suspend fun loadUpComingMovies(page: Int): MovieResponse =
        apiService.loadUpComingMovies(page, BuildConfig.API_KEY)

    override suspend fun loadMovieDetails(movieId: Int): MovieDetail =
        apiService.loadMovieDetails(movieId, BuildConfig.API_KEY)

    override suspend fun searchMoviesByQuery(query: String, page: Int): MovieResponse =
        apiService.searchMoviesByQuery(query, page, BuildConfig.API_KEY)

    override suspend fun loadGenres(): GenreResponse =
        apiService.loadGenres(BuildConfig.API_KEY)



}
