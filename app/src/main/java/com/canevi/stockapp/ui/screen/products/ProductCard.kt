package com.canevi.stockapp.ui.screen.products

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.canevi.stockapp.model.Product
import com.canevi.stockapp.model.dto.ProductDTO
import com.canevi.stockapp.repository.ProductOrchestrationRepository
import kotlinx.coroutines.launch

@Composable
fun ProductCard(
    product: Product,
    onNavigateToProductBuy: (Product) -> Unit,
) {
    val productOrchestrationRepository = ProductOrchestrationRepository(LocalContext.current)
    val coroutineScope = rememberCoroutineScope()
    var dto by remember { mutableStateOf<ProductDTO?>(null) }

    Card(
        onClick = {
            if (dto == null) {
                coroutineScope.launch {
                    dto = productOrchestrationRepository.readProduct(product.id.toString())
                }
            }
            if(dto != null) {
                onNavigateToProductBuy(product)
            } else {
                /// some error message
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(.8f),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = .4f))
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            )
            Column(modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp)) {
                Text(
                    text = product.name,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(top = 8.dp)
                )
            }
        }
    }
}