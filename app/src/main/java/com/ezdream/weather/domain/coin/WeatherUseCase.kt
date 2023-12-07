package com.ezdream.weather.domain.coin

import com.ezdream.weather.network.UseCase
import com.ezdream.weather.repository.weatherService.WeatherRepository
import com.ezdream.weather.repository.weatherService.reqres.WeatherResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherUseCase @Inject constructor(private val repository: WeatherRepository) :
    UseCase<WeatherUseCase.Params, WeatherResponse>() {

    data class Params(
        val lan: String,
        val lon: String,
        val key: String,
    )

    override fun execute(parameter: Params?): Flow<WeatherResponse> {

        return repository.getWeather(parameter!!.lon,parameter.lan,parameter.key,"metric")
    }

}