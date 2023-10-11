package com.example.myapplication


//Environment Variables (Normally pulled from gradle/build config and auto generated but hard coded here for simplicity)
class Env {


    companion object {
        const val WEATHER_API_BASE_URL = "https://api.openweathermap.org/data/2.5/"
        const val GEO_API_BASE_URL = "https://api.openweathermap.org/geo/1.0/"
        const val WEATHER_API_KEY = "fee5cb238948a8300da02aed6f84334d"
        const val WEATHER_ICON_BASE_URL = "https://openweathermap.org/img/wn/"
        const val WEATHER_ICON_EXT = "@2x.png"
        const val SHARED_PREFS_KEY = "sharedPrefs"
        const val SHARED_PREFS_LAT_KEY = "lastLat"
        const val SHARED_PREFS_LONG_KEY = "lastLong"
    }

}