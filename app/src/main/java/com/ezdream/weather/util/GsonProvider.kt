package com.ezdream.weather.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object GsonProvider {

    var gson: Gson = GsonBuilder().serializeNulls().create()
}