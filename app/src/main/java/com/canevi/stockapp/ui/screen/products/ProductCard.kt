package com.canevi.stockapp.ui.screen.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.canevi.stockapp.model.Product

@Composable
fun ProductCard(
    product: Product,
    onNavigateToProductBuy: (Product) -> Unit,
) {
    Card(
        onClick = {
            onNavigateToProductBuy(product)
        },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f), // Keep the card square-shaped
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = product.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(bottom = 8.dp, top = 8.dp)
                    .then(Modifier.fillMaxWidth())
            )
            Spacer(modifier = Modifier.fillMaxSize())
            Text(
                text = product.quantity.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}