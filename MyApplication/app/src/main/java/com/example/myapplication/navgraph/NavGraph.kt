package com.example.myapplication.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.myapplication.home.HomeScreen

@Composable
fun App(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController,
        startDestination = NavRoute.HOME.route) {
            navGraph()
    }
}

fun NavGraphBuilder.navGraph(
) {

    navigation(
        route = NavRoute.HOME.route,
        startDestination = NavRoute.HOME.screen
    ) {
        composable(
            route = NavRoute.HOME.screen
        ) {
            HomeScreen()
        }
    }
}
