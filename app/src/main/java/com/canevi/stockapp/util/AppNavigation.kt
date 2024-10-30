package com.canevi.stockapp.util

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.canevi.stockapp.model.Product
import com.canevi.stockapp.ui.screen.productdetail.BuyProductScreen
import com.canevi.stockapp.ui.screen.productdetail.NewProductScreen
import com.canevi.stockapp.ui.screen.products.ProductGridScreen
import kotlinx.serialization.Serializable

@Serializable
object ProductList

@Serializable
object NewProduct

@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = ProductList,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }) {
        composable<ProductList> {
            ProductGridScreen(
                onNavigateToProductBuy = { product ->
                    navController.navigate(route = product)
                },
                onNavigateToNewProduct = {
                    navController.navigate(route = NewProduct)
                }
            )
        }
        composable<Product>(
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) { backStackEntry ->
            val product = backStackEntry.toRoute<Product>()
            BuyProductScreen(
                product = product,
                onNavigateToProductList = {
                    navController.navigate(route = ProductList)
                }
            )
        }
        composable<NewProduct>(
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            NewProductScreen(
                onNavigateToProductList = {
                    navController.navigate(route = ProductList)
                }
            )
        }
    }
}