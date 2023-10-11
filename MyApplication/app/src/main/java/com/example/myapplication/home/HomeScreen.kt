package com.example.myapplication.home

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.myapplication.home.model.HomeViewData


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController = rememberNavController(),
    viewModel: HomeViewModel = hiltViewModel()
) {

    var query by remember { mutableStateOf ("") }
    var active by remember { mutableStateOf(false) }
    val items = remember { viewModel.locationsFromQuery }
    val context = LocalContext.current

    val requestPermission = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            viewModel.getLocation(context)
        }
    }
    LaunchedEffect(Unit) {
        when {
            viewModel.getLastLocation(context) -> {
                viewModel.getWeatherForLastLocation()
            }
            viewModel.hasLocationPermissions(context) -> {
                viewModel.getLocation(context)
            }
            else -> {
                requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    if (viewModel.errorState.value) {
        AlertDialog(onDismissRequest = { viewModel.errorState.value = false }, confirmButton = {
            Button(
                onClick = {
                    viewModel.errorState.value = false
                }) {
                Text("OK")
            }
        }, title = { Text("Error") }, text = { Text("Something went wrong. Please try again.") })
    }


    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth(),
                query = query,
                onQueryChange = {
                    query = it
                    viewModel.updateQuery(it)
                }, onSearch = {
                    active = false
                }, active = active,
                onActiveChange = {
                    active = it
                },
                placeholder = {
                    Text(text="Search")
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }
            )
            {
                items.value?.forEach {
                    Row(modifier = Modifier
                        .padding(12.dp)
                        .clickable {
                            viewModel.getWeatherForLocation(it.lat, it.long)
                            viewModel.saveLastLocation(it.lat, it.long, context)
                            active = false
                        }) {
                        Text("${it.name}, " +
                                it.state?.let {
                                    "$it, "
                                } +
                                it.country)
                    }
                }
            }
            viewModel.homeViewData.value?.let { homeViewData ->
                CurrentWeather(iconUrl = viewModel.getWeatherIcon(homeViewData.iconCode), homeViewData)
            } ?: EmptyView()
        }
    }
}

@Composable
fun CurrentWeather(
    iconUrl: String,
    homeViewData : HomeViewData
) {
    Column(Modifier.padding(24.dp)) {
        Text(text = "Current weather in ${homeViewData.city}", textAlign = TextAlign.Center, fontSize = 32.sp, modifier = Modifier.padding(top = 16.dp, bottom = 32.dp), lineHeight = 32.sp)
        Text(text = "${homeViewData.temp}F", fontSize = 24.sp, textAlign = TextAlign.Center, modifier = Modifier.align(CenterHorizontally))
        AsyncImage(model = iconUrl, contentDescription = "Weather Icon", modifier = Modifier
            .align(CenterHorizontally)
            .width(64.dp)
            .height(64.dp), contentScale = ContentScale.FillBounds)
        Text(text = "Feels like: ${homeViewData.feelsLike}F", modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))
        Text(text = "High/Low: ${homeViewData.high}F / ${homeViewData.low}F", modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))
        Text(text = "Humidity: ${homeViewData.humidity}%",  modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))
        Text(text = "Wind: ${homeViewData.wind} mph",  modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))
        Text(text = "Visibility: ${homeViewData.visibility}m",  modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))
    }
}

@Composable
fun EmptyView() {
    Text(text = "To get started use the search bar above for a city to get the current weather", textAlign = TextAlign.Center, fontSize = 24.sp, modifier = Modifier.padding(top = 16.dp, bottom = 32.dp), lineHeight = 32.sp)

}

