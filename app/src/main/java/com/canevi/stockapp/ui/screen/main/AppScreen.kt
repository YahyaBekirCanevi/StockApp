package com.canevi.stockapp.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.canevi.stockapp.model.Product
import com.canevi.stockapp.ui.component.BottomAppBar
import com.canevi.stockapp.ui.screen.products.ProductGridScreen
import com.canevi.stockapp.ui.screen.profile.ProfileScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(
    onNavigateToProductBuy: (Product) -> Unit,
    onNavigateToNewProduct: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    var currentPage by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(key1 = currentPage) {
        coroutineScope.launch {
            pagerState.scrollToPage(page = currentPage)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = Modifier.weight(1f)
        ) { page ->
            if (page == 0) {
                ProductGridScreen(
                    onNavigateToProductBuy = onNavigateToProductBuy
                )
            } else {
                ProfileScreen()
            }
        }
        BottomAppBar(
            showNewProductScreen = { onNavigateToNewProduct() },
            onHomeScreen = {
                currentPage = 0
            },
            onProfileScreen = {
                currentPage = 1
            }
        )
    }
}