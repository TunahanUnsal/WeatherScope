package com.ezdream.weather.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.ezdream.weather.R
import com.ezdream.weather.domain.coin.WeatherUseCase
import com.github.matteobattilana.weather.PrecipType
import com.github.matteobattilana.weather.WeatherView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject


@HiltViewModel
class HomeActivityVM @Inject constructor(
    private val weatherUseCase: WeatherUseCase
) : ViewModel() {

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    suspend fun getWeatherFun(
        activity: Activity,
        temp: TextView,
        feel: TextView,
        city: TextView,
        imageView: ImageView,
        lon: String,
        lan: String,
        view: WeatherView
    ) {

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
                temp.text = it.main?.temp?.toInt().toString() + " C°"
                feel.text = "(feels like: ${it.main?.feelsLike?.toInt()} C°)"
                city.text = it.name
                when (it.weather[0].main) {
                    "Rain" -> {imageView.setImageDrawable(activity.getDrawable(R.drawable.rain)); anim(view,1,activity) }
                    "Thunderstorm" -> {imageView.setImageDrawable(activity.getDrawable(R.drawable.thunderstorm));anim(view,3,activity)}
                    "Drizzle" -> {imageView.setImageDrawable(activity.getDrawable(R.drawable.drizzle));anim(view,3,activity)}
                    "Snow" -> {imageView.setImageDrawable(activity.getDrawable(R.drawable.snow));anim(view,2,activity)}
                    "Atmosphere" -> {imageView.setImageDrawable(activity.getDrawable(R.drawable.atmosphere));anim(view,3,activity)}
                    "Clear" -> {imageView.setImageDrawable(activity.getDrawable(R.drawable.clear));anim(view,0,activity)}
                    "Clouds" -> {imageView.setImageDrawable(activity.getDrawable(R.drawable.clouds));anim(view,3,activity)}
                    else -> {imageView.setImageDrawable(activity.getDrawable(R.drawable.clear));anim(view,3,activity)}
                }

            }
        }
    }

    fun anim(view: WeatherView,mod:Int,activity: Activity) {
        activity.runOnUiThread {
            when(mod){
                0->view.setWeatherData(PrecipType.CLEAR)
                1->view.setWeatherData(PrecipType.RAIN)
                2->view.setWeatherData(PrecipType.SNOW)
                3->view.setWeatherData(PrecipType.CLEAR)
            }
        }
    }
}