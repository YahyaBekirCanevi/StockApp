package com.canevi.stockapp.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onSubMenu: (Screen) -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = 4.dp,
                )
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .width(120.dp)
                    .aspectRatio(1f)
                    .background(Color.Gray, RoundedCornerShape(60.dp))
            )
            Text(
                "Profile Name",
                fontSize = 26.sp, fontWeight = FontWeight.W700, textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp, bottom = 8.dp)
            )
            Row {
                Text(
                    "${88} Sold Items | ${128} Followers",
                    fontSize = 16.sp, fontWeight = FontWeight.W400, color = Color.Gray, textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                )
            }
            ProfileCard("Account") { onSubMenu(Screen.Account) }
            ProfileCard("Notification Settings") { onSubMenu(Screen.NotificationSettings) }

            Text(
                "Products",
                fontSize = 16.sp, fontWeight = FontWeight.W700, textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 16.dp)
                    .padding(top = 12.dp)
            )
            ProfileCard("Liked") { onSubMenu(Screen.LikedProducts) }
            ProfileCard("My Products") { onSubMenu(Screen.MyProducts) }

            Text(
                "History",
                fontSize = 16.sp, fontWeight = FontWeight.W700, textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 16.dp)
                    .padding(top = 12.dp)
            )
            ProfileCard("Buy History") { onSubMenu(Screen.BuyHistory) }
            ProfileCard("Search History") { onSubMenu(Screen.SearchHistory) }
        }
    }
}

@Composable
fun ProfileCard(label: String, onClick: () -> Unit) = Card(
    onClick = { onClick() },
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp),
    shape = RoundedCornerShape(8.dp),
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f),
        contentColor = MaterialTheme.colorScheme.onSurface
    )
) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            fontSize = 16.sp, fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
        Icon(
            Icons.AutoMirrored.Rounded.KeyboardArrowRight, "",
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}