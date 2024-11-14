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
import androidx.navigation.toRoute
import com.canevi.stockapp.model.Product
import com.canevi.stockapp.ui.screen.main.AppScreen
import com.canevi.stockapp.ui.screen.productdetail.BuyProductScreen
import com.canevi.stockapp.ui.screen.productdetail.NewProductScreen
import kotlinx.serialization.Serializable

@Serializable
object AppScreen

@Serializable
object NewProduct

@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = AppScreen,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }) {
        composable<AppScreen> {
            AppScreen(
                onNavigateToProductBuy = { product ->
                    navigateToTap(navController, product)
                },
                onNavigateToNewProduct = {
                    navigateToTap(navController, NewProduct)
                },
            )
        }
        composable<Product>(
            enterTransition = { enterTransition(Start) },
            exitTransition = { exitTransition(End) }
        ) { backStackEntry ->
            val product = backStackEntry.toRoute<Product>()
            BuyProductScreen(
                product = product,
                onNavigateToProductList = {
                    navController.popBackStack()
                }
            )
        }
        composable<NewProduct>(
            enterTransition = { enterTransition(Up) },
            exitTransition = { exitTransition(Down) }
        ) {
            NewProductScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
fun <T : Any> navigateToTap(navController: NavHostController, route: T) {
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
