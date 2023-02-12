package com.udacity.asteroidradar.data.remote

import com.udacity.asteroidradar.models.PictureOfDay
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidApi {
    @GET(value = "neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query(value = "start_date") startDate: String,
        @Query(value = "end_date") endDate: String,
        @Query("api_key")apiKey:String
    ): Response<String>

    @GET(value = "planetary/apod")
    suspend fun getPictureOfTheDay(
        @Query("api_key")apiKey:String
    ): Response<PictureOfDay>
}