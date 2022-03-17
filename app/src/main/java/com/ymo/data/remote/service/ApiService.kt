package com.ymo.data.remote.service

import com.ymo.data.model.api.MovieDetail
import com.ymo.data.model.api.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("upcoming")
    suspend fun loadUpComingMovies(
        @Query("api_key") apiKey: String
    ): MovieResponse


    @GET("popular")
    suspend fun loadTopRatedMovies(
        @Query("api_key") apiKey: String
    ): MovieResponse


    @GET("{movie_id}")
    suspend fun loadMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MovieDetail
}
