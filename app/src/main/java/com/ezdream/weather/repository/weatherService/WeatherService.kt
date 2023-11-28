package com.ezdream.weather.repository.weatherService

import com.ezdream.weather.repository.weatherService.reqres.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {


    @GET("data/2.5/weather?")
    suspend fun getWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") key: String
    ): WeatherResponse
}