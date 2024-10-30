package com.canevi.stockapp.ui.screen.products

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canevi.stockapp.model.Product
import com.canevi.stockapp.repository.ProductRepository
import com.canevi.stockapp.ui.component.BottomAppBar
import com.canevi.stockapp.ui.component.SearchBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

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
            val fetchedProducts = withContext(Dispatchers.IO) {
                productRepository.getProducts()
            }
            withContext(Dispatchers.Main) {
                if (fetchedProducts.isEmpty())
                    snackBarHostState.showSnackbar("No Data Shown or Server Error")
                products.clear()
                products.addAll(fetchedProducts)
                isRefreshing = false
                isLoading = false
            }
        }
    }

    // Fetch products when the screen loads
    LaunchedEffect(Unit) {
        loadProducts()
    }

    val categoriesAppBarScrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val pullRefreshState = rememberPullToRefreshState()
    val scrollState = rememberScrollState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        bottomBar = {
            BottomAppBar(
                showNewProductScreen = { onNavigateToNewProduct() },
                onHomeScreen = {},
                onProfileScreen = {}
            )
        },
        topBar = {
            TopAppBar(
                expandedHeight = 106.dp,
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                navigationIcon = {},
                title = {
                    Row(
                        modifier = Modifier
                            .padding(top = 56.dp, bottom = 8.dp)
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .layout { measurable, constraints ->
                                val paddingCompensation = 16.dp
                                    .toPx()
                                    .roundToInt()
                                val adjustedConstraints = constraints.copy(
                                    // not a good idea inside horizontal scroll view,
                                    // but I guess we can assume that's not the case here
                                    maxWidth = constraints.maxWidth + paddingCompensation
                                )
                                val placeable = measurable.measure(adjustedConstraints)
                                layout(placeable.width, placeable.height) {
                                    placeable.place(-paddingCompensation / 2, 0)
                                }
                            }
                            .horizontalScroll(scrollState)
                    ) {
                        var categories = listOf("Woman", "Baby", "House", "Man", "Technology")
                        categories.map {
                            Row(
                                modifier = Modifier
                                    .padding(
                                        start = if(categories.indexOf(it) == 0) 16.dp else 0.dp,
                                        end = 8.dp
                                    )
                                    .background(
                                        MaterialTheme.colorScheme.primary.copy(alpha = .5f),
                                        RoundedCornerShape(16.dp)
                                    )
                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Absolute.SpaceAround
                            ) {
                                Text(
                                    it, fontSize = 14.sp, minLines = 1, maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                    modifier = Modifier.widthIn(min = 16.dp, max = 256.dp)
                                )
                            }
                        }
                    }
                }, scrollBehavior = categoriesAppBarScrollBehavior
            )
            TopAppBar(expandedHeight = 48.dp, title = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .height(48.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.SpaceAround
                ) {
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
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
                    }
                    IconButton(onClick = {}, modifier = Modifier.padding(end = 8.dp)) {
                        Icon(
                            Icons.Filled.Notifications,
                            "Notifications",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            })

        },
        modifier = Modifier
            .nestedScroll(categoriesAppBarScrollBehavior.nestedScrollConnection),
        content = { paddingValues ->
            PullToRefreshBox(
                isRefreshing = !isLoading && isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.padding(paddingValues),
                onRefresh = { loadProducts() }
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
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

