package com.ezdream.weather.repository.weatherService

import com.ezdream.weather.network.ApiRepository
import com.ezdream.weather.repository.weatherService.reqres.WeatherResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val service: WeatherService) : ApiRepository(),
    WeatherRepository {


    override fun getWeather(lat: String?, lon: String?, key: String?,units:String?): Flow<WeatherResponse> =
        sendRequest { service.getWeather(lat!!, lon!!, key!!, units!!) }

}