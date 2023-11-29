package com.ezdream.weather.repository.weatherService

import com.ezdream.weather.repository.weatherService.reqres.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getWeather(lat : String?,lon:String?,key:String?,units:String?): Flow<WeatherResponse>

}