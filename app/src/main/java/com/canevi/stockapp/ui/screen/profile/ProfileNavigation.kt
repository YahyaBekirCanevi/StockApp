package com.canevi.stockapp.ui.screen.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import com.canevi.stockapp.ui.screen.profile.sub.AccountScreen
import com.canevi.stockapp.ui.screen.profile.sub.MyProductsScreen
import kotlinx.coroutines.launch

@Composable
fun ProfileNavigation() {
    val pageValue = rememberPagerState(initialPage = 0) { 2 }
    val coroutineScope = rememberCoroutineScope()
    var (page, setPage) = remember { mutableStateOf(Screen.Account) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            pageValue.scrollToPage(0)
        }
    }

    BackHandler(enabled = pageValue.currentPage == 1) {
        coroutineScope.launch {
            pageValue.animateScrollToPage(0)
        }
    }

    HorizontalPager(state = pageValue, userScrollEnabled = false) { i ->
        if (i == 0)
            ProfileScreen(onSubMenu = {
                setPage(it)
                coroutineScope.launch {
                    pageValue.animateScrollToPage(1)
                }
            })
        else {
            when (page) {
                Screen.Account -> AccountScreen()
                Screen.NotificationSettings -> Text("NotificationSettings", color = Color.White)
                Screen.LikedProducts -> Text("LikedProducts", color = Color.White)
                Screen.MyProducts -> MyProductsScreen()
                Screen.BuyHistory -> Text("BuyHistory", color = Color.White)
                Screen.SearchHistory -> Text("SearchHistory", color = Color.White)
            }
        }
    }
}