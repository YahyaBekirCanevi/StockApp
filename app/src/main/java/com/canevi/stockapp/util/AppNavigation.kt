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
                //saveState = true:
                //It allows saving the states of these fragments during the process of clearing the fragment/states up to the point specified with popUpTo. In this way, the contents of the fragments are not lost.
            }
            restoreState = true
            //It allows restoring the old fragment states after the redirection process. In this way, when the user returns to the navigation history, he can see the old states of the fragments.
            launchSingleTop = true
            //During the routing process to the target route, if the target route is already at the top (existing one), it allows using the existing instance instead of creating a new instance. This prevents a page from being opened repeatedly.
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
