package com.ymo.data.remote.service

import com.ymo.data.model.api.GenreResponse
import com.ymo.data.model.api.MovieDetail
import com.ymo.data.model.api.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/now_playing")
    suspend fun loadNowPlayingMovies(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): MovieResponse

    @GET("movie/popular")
    suspend fun loadPopularMovies(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): MovieResponse

    @GET("movie/top_rated")
    suspend fun loadTopRatedMovies(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): MovieResponse


    @GET("movie/upcoming")
    suspend fun loadUpComingMovies(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): MovieResponse

    @GET("search/movie")
    suspend fun searchMoviesByQuery(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun loadMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MovieDetail

    @GET("genre/movie/list")
    suspend fun loadGenres(@Query("api_key") apiKey: String): GenreResponse
}
