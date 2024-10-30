package com.canevi.stockapp.ui.screen.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.canevi.stockapp.ui.component.BottomAppBar
import com.canevi.stockapp.ui.component.SearchBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateToNewProduct: () -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        bottomBar = {
            BottomAppBar(
                showNewProductScreen = { onNavigateToNewProduct() }
            )
        },
        topBar = {
            TopAppBar(
                title = { Text("Yahya Bekir Canevi") },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        content = { innerPadding ->
            Column(modifier = Modifier.consumeWindowInsets(innerPadding)) {

            }
        }
    )
}