package com.ezdream.weather.ui.home

import android.app.Activity
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.ezdream.weather.BuildConfig
import com.ezdream.weather.domain.coin.WeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class HomeActivityVM @Inject constructor(
    private val weatherUseCase: WeatherUseCase
) : ViewModel() {

    suspend fun getWeatherFun(activity: Activity, textView: TextView, lon:String, lan:String) {

        weatherUseCase.invoke(
            WeatherUseCase.Params(
                lon = lon,
                lan = lan,
                key = "5407b359d0c26a1a938ec299c8154a83"
            )
        ).onStart {
            Log.i("TAG", "getWeatherFun onStart")
        }.catch {
            Log.i("TAG", "getWeatherFun: $it")
        }.collect {
            Log.i("TAG", "getWeatherFun: $it")
            activity.runOnUiThread {
                textView.text = it.toString()
            }
        }
    }
}