package com.canevi.stockapp.ui.screen.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.canevi.stockapp.model.Product
import com.canevi.stockapp.repository.ProductRepository
import com.canevi.stockapp.ui.component.BottomAppBar
import com.canevi.stockapp.ui.component.SearchBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductGridScreen(
    onNavigateToProductBuy: (Product) -> Unit,
    onNavigateToNewProduct: () -> Unit,
) {
    val productRepository = ProductRepository(LocalContext.current)
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val products = remember { mutableStateListOf<Product>() }
    var isLoading by remember { mutableStateOf(true) }
    var isRefreshing by remember { mutableStateOf(false) }

    fun loadProducts() {
        coroutineScope.launch {
            isRefreshing = true
            val fetchedProducts = productRepository.getProducts()
            if (fetchedProducts.isEmpty())
                snackBarHostState.showSnackbar("No Data Shown or Server Error")
            products.clear()
            products.addAll(fetchedProducts)
            isRefreshing = false
            isLoading = false
        }
    }

    // Fetch products when the screen loads
    LaunchedEffect(Unit) {
        loadProducts()
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val pullRefreshState = rememberPullToRefreshState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        bottomBar = {
            BottomAppBar(
                showNewProductScreen = { onNavigateToNewProduct() }
            )
        },
        topBar = {
            TopAppBar(title = {
                SearchBar(onSearch = { query ->
                    coroutineScope.launch {
                        val result = if (query.isNotEmpty()) {
                            productRepository.searchProducts(query)
                        } else {
                            productRepository.getProducts()
                        }
                        products.clear()
                        products.addAll(result)
                    }
                })
            }, scrollBehavior = scrollBehavior)
        },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        content = { paddingValues ->
            PullToRefreshBox(
                isRefreshing = !isLoading && isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.padding(paddingValues),
                onRefresh = { loadProducts() }
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(products.count()) { index ->
                        ProductCard(products[index], onNavigateToProductBuy)
                    }
                }
            }
        }
    )
}

