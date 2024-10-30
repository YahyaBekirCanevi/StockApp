package com.canevi.stockapp.ui.screen.productdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.canevi.stockapp.model.Product
import com.canevi.stockapp.model.dto.DetailedProductDTO
import com.canevi.stockapp.repository.ProductRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyProductScreen(
    product: Product,
    onNavigateToProductList: () -> Unit,
) {
    val productRepository = ProductRepository(LocalContext.current)
    val snackbarHostState = remember { SnackbarHostState() }

    val nameScrollState = rememberScrollState()
    val sizeScrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    var selectedSize by remember { mutableStateOf("null") }
    var descriptionToggle by remember { mutableStateOf(false) }
    var dto: DetailedProductDTO? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            if (product.id == null) {
                snackbarHostState.showSnackbar("No Data Shown or Server Error")
                onNavigateToProductList()
                return@launch
            }
            val detailedProductDTO = productRepository.getProductDetail(productId = product.id)
            if(detailedProductDTO == null) {
                snackbarHostState.showSnackbar("No Data Shown or Server Error")
                onNavigateToProductList()
                return@launch
            }
            dto = detailedProductDTO
            selectedSize = dto?.size.toString()
        }
    }


    Scaffold(
        bottomBar = {
            Row(modifier = Modifier
                .padding(18.dp)
                .height(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Price",
                    modifier = Modifier.weight(2f)
                )
                ElevatedButton(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.elevatedButtonColors()
                        .copy(containerColor = Color(0.2f, 0.6f,0.2f, alpha = 1f)),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Rounded.ShoppingCart,
                        contentDescription = "Buy",
                        tint = Color.White,
                        modifier = Modifier
                            .width(24.dp)
                            .aspectRatio(1f)
                    )
                    Text(
                        text = "Buy",
                        modifier = Modifier.padding(horizontal = 4.dp),
                        style = MaterialTheme.typography.labelLarge.copy(color = Color.White)
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            TopAppBar(title = {
                Row(modifier = Modifier.wrapContentHeight()) {
                    IconButton(
                        modifier = Modifier.padding(4.dp),

                        onClick = {
                            onNavigateToProductList()
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Rounded.KeyboardArrowLeft, contentDescription = "Back")
                    }
                    Box(modifier = Modifier.weight(1f))
                }
            }, colors = TopAppBarDefaults.topAppBarColors().copy(
                containerColor = Color.Transparent,
                scrolledContainerColor = MaterialTheme.colorScheme.background,
            ))
        },
        content = {
            Column(modifier = Modifier.padding(top = it.calculateTopPadding() * 0)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp)
                        .background(color = Color.Gray)
                ) {
                    Text(text = dto?.id ?: "")
                }
                Text(
                    text = dto?.name ?: "",
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(8.dp)
                        .horizontalScroll(nameScrollState)
                )
                Row(
                    modifier = Modifier.horizontalScroll(sizeScrollState),
                ) {
                    listOf("S", "M", "L", "XL", "XXL", "3XL", "4XL", "5XL").map {
                        ElevatedAssistChip(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            onClick = { selectedSize = it },
                            enabled = it != selectedSize,
                            label = {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        color = Color.Gray,
                                        fontSize = TextUnit(1f, TextUnitType(0))
                                    ),
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                        )
                    }
                }
                if (dto?.description != null)
                    Column(modifier = Modifier.padding(8.dp)) {
                        TextButton(
                            onClick = { descriptionToggle = !descriptionToggle },
                            modifier = Modifier.padding(0.dp)
                        ) {
                            Row {
                                Text(
                                    text = "Description",
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .weight(1f)
                                )
                                Icon(
                                    if (descriptionToggle) Icons.Filled.ArrowDropDown else Icons.Filled.ArrowDropDown,
                                    contentDescription = "Description",
                                    tint = Color.Gray
                                )
                            }
                        }
                        if (descriptionToggle)
                            Text(
                                text = dto?.description ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                    }
            }
        }
    )
}

@Preview
@Composable
fun BuyProductScreenPreview() {
    val product = Product(
        id = "f882ebcf-f3d2-4677-b48c-283c23e36f05",
        name = "test data",
        quantity = 12
    )
    BuyProductScreen(product = product, onNavigateToProductList = {})
}