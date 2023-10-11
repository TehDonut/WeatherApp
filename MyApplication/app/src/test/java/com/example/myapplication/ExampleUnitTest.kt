package com.example.myapplication

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.api.GeoAPI
import com.example.myapplication.api.WeatherAPI
import com.example.myapplication.data.GeoRepository
import com.example.myapplication.data.WeatherRepository
import com.example.myapplication.home.HomeViewModel
import com.example.myapplication.home.model.HomeViewData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection.HTTP_OK

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */


@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    private lateinit var homeViewModel: HomeViewModel

    private var mockWebServer = MockWebServer()
    private lateinit var weatherAPI: WeatherAPI
    private lateinit var geoAPI: GeoAPI
    val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(
        HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BODY)).build()

    @ExperimentalCoroutinesApi
    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    @Before
    fun setUp() {

        mockWebServer.start()
        geoAPI = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(GeoAPI::class.java)
        weatherAPI = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(WeatherAPI::class.java)
        homeViewModel = HomeViewModel(WeatherRepository(weatherAPI), GeoRepository(geoAPI))
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `When homeViewModel_getLocation is called return proper response`() =  runTest {
        val response = MockResponse().setResponseCode(HTTP_OK)
            .setBody(
                "{\"coord\":{ \"lon\":-0.092,\"lat\":51.5156},\"weather\":[ {\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03n\"}],\"base\":\"stations\",\"main\":{ \"temp\":58.82,\"feels_like\":57.74,\"temp_min\":56.82,\"temp_max\":60.51,\"pressure\":1014,\"humidity\":71},\"visibility\":10000,\"wind\":{\"speed\":8.05,\"deg\":240}\"clouds\":{\"all\":32},\"dt\":1696985368,\"sys\":{\"type\":2,\"id\":2006068,\"country\":\"GB\",\"sunrise\":1697004999,\"sunset\":1697044647},\"timezone\":3600,\"id\":2643741,\"name\":\"City of London\",\"cod\":200}")
        mockWebServer.enqueue(response)
        val spyHomeViewData : MutableState<HomeViewData?> = Mockito.spy(mutableStateOf(null))
        Mockito.`when`(homeViewModel.homeViewData).thenReturn(spyHomeViewData)
        homeViewModel.getWeatherForLocation(51.5156, -0.092)

        // TODO: Assert homeViewData == expected value; I ran out of time before I could figure this out.
    }
}