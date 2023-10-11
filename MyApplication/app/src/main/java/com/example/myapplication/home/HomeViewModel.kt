package com.example.myapplication.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Env
import com.example.myapplication.data.GeoRepository
import com.example.myapplication.data.WeatherRepository
import com.example.myapplication.data.model.Geo
import com.example.myapplication.home.model.HomeViewData
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val geoRepository: GeoRepository
) : ViewModel() {
    private val queryString = MutableStateFlow("")
    val locationsFromQuery : MutableState<List<Geo>?> = mutableStateOf(null)
    val homeViewData : MutableState<HomeViewData?> = mutableStateOf(null)
    val errorState = mutableStateOf(false)
    var lastLocation : Pair<Double, Double>? = null

    fun getWeatherIcon(code: String) : String{
        return Env.WEATHER_ICON_BASE_URL + code + Env.WEATHER_ICON_EXT
    }

    init {
        // We want to debounce user input so it doesn't rapid fire call the geo API every time the user types
        viewModelScope.launch {
            queryString.debounce(1000).distinctUntilChanged().collect {
                debounceQueryString(it)
            }
        }
    }

    fun getWeatherForLocation(lat: Double, long: Double) {
        viewModelScope.launch {
            val response = weatherRepository.getCurrentWeather(lat, long)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let { currentWeatherResponse ->
                        homeViewData.value = HomeViewData.fromWeatherResponse(currentWeatherResponse)
                    }

                }
                else {
                    errorState.value = true
                }
            }
        }
    }

    fun getLastLocation(context: Context): Boolean {
        val prefs = context.getSharedPreferences(Env.SHARED_PREFS_KEY, Context.MODE_PRIVATE)
        val lastLat = prefs.getString(Env.SHARED_PREFS_LAT_KEY, "")
        val lastLong = prefs.getString(Env.SHARED_PREFS_LONG_KEY, "")
        if (lastLat?.isNotBlank() == true && lastLong?.isNotBlank() == true) {
            lastLocation = Pair(lastLat.toDouble(), lastLong.toDouble())
            return true
        }
        return false
    }

    fun getWeatherForLastLocation() {
        lastLocation?.first?.let { lat ->
            lastLocation?.second?.let { long ->
                getWeatherForLocation(lat, long)
            }
        }
    }

    fun saveLastLocation(lat: Double, long: Double, context: Context) {
        val prefs = context.getSharedPreferences(Env.SHARED_PREFS_KEY, Context.MODE_PRIVATE)
        prefs.edit().putString(Env.SHARED_PREFS_LAT_KEY, lat.toString()).putString(Env.SHARED_PREFS_LONG_KEY, long.toString()).apply()
    }

    fun hasLocationPermissions(context: Context) : Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun getLocation(context: Context) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener {
            //This method returns null for locations on emulators, but works fine on physical devices...
                if (it != null) {
                    getWeatherForLocation(it.latitude, it.longitude)
                }
            }.addOnFailureListener {
                Log.e("Request Location: ", it.message.toString())
        }
    }

    fun updateQuery(query: String) {
        queryString.value = query
    }

    fun debounceQueryString(query: String) {
        if (query.isNotBlank()) {
            queryForLocations(query)
        }
    }

    fun queryForLocations(query: String) {
        viewModelScope.launch {
            val response = geoRepository.getLocationsQuery(query)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let { listOfGeo ->
                        locationsFromQuery.value = listOfGeo
                    }
                }
                else {
                    errorState.value = true
                }
            }
        }
    }
}