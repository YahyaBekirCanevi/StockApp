package com.canevi.stockapp.util

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Down
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.End
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Start
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Up
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.canevi.stockapp.model.Product
import com.canevi.stockapp.ui.screen.main.AppScreen
import com.canevi.stockapp.ui.screen.productdetail.BuyProductScreen
import com.canevi.stockapp.ui.screen.productdetail.NewProductScreen
import com.canevi.stockapp.ui.screen.productdetail.UpdateProductScreen
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

fun String.encodeForRoute(): String = this.replace("{", "%7B").replace("}", "%7D").replace("\"", "%22")

@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = "app_screen",
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }) {
        composable("app_screen") {
            AppScreen(
                onNavigateToProductBuy = {
                    val productJson = Json.encodeToString(serializer<Product>(), it)
                    navigateTo(navController, "buy_product?product=${productJson.encodeForRoute()}")
                },
                onNavigateToProductUpdate = {
                    val productJson = Json.encodeToString(serializer<Product>(), it)
                    navigateTo(navController, "update_product?product=${productJson.encodeForRoute()}")
                },
                onNavigateToNewProduct = {
                    navigateTo(navController, "new_product")
                },
            )
        }
        composable(
            "new_product",
            enterTransition = { enterTransition(Up) },
            exitTransition = { exitTransition(Down) }
        ) {
            NewProductScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            "buy_product?product={product}",
            enterTransition = { enterTransition(Start) },
            exitTransition = { exitTransition(End) }
        ) { backStackEntry ->
            val product = backStackEntry.decodeRoute<Product>("product")
            BuyProductScreen(
                product = product, onBack = { navController.popBackStack() }
            )
        }
        composable(
            "update_product?product={product}",
            enterTransition = { enterTransition(Up) },
            exitTransition = { exitTransition(Down) }
        ) { backStackEntry ->
            val product = backStackEntry.decodeRoute<Product>("product")
            UpdateProductScreen(
                product = product, onBack = { navController.popBackStack() }
            )
        }
    }
}

inline fun <reified T : Any> NavBackStackEntry.decodeRoute(route: String): T {
    val routeJson = arguments?.getString(route)
        ?: throw IllegalArgumentException("Missing argument")
    return Json.decodeFromString(routeJson)
}

fun navigateTo(navController: NavHostController, route: String) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { homeScreen ->
            popUpTo(homeScreen) {
                saveState = true
            }
            restoreState = true
            launchSingleTop = true
        }
    }
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(
    direction: AnimatedContentTransitionScope.SlideDirection
): @JvmSuppressWildcards ExitTransition = fadeOut(
    animationSpec = tween(
        300, easing = LinearEasing
    )
) + slideOutOfContainer(
    animationSpec = tween(300, easing = EaseOut),
    towards = direction
)

fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(
    direction: AnimatedContentTransitionScope.SlideDirection
): @JvmSuppressWildcards EnterTransition? = fadeIn(
    animationSpec = tween(
        300, easing = LinearEasing
    )
) + slideIntoContainer(
    animationSpec = tween(300, easing = EaseIn),
    towards = direction
)
