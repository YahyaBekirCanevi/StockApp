package com.canevi.stockapp.ui.screen.productdetail

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canevi.stockapp.model.Product
import com.canevi.stockapp.repository.ProductRepository
import com.canevi.stockapp.ui.theme.StockAppTheme
import java.util.Base64

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyProductScreen(
    product: Product,
    onNavigateToProductList: () -> Unit,
) {
    val productRepository = ProductRepository(LocalContext.current)
    val snackbarHostState = remember { SnackbarHostState() }

    val scrollState = rememberScrollState()
    val nameScrollState = rememberScrollState()
    var descriptionToggle by remember { mutableStateOf(false) }

    var categories = remember { mutableMapOf<String, String>() }
    var images = remember { mutableMapOf<String, ByteArray>() }

    var imagePagerState = remember { PagerState(pageCount = { 0 }) }

    LaunchedEffect(Unit) {
        categories = productRepository.getCategoriesOfProduct(product.id.toString()).toMutableMap()
        productRepository.getImagesForProduct(product.id.toString()).forEach {
            images.put(it.key, Base64.getDecoder().decode(it.value))
        }
        imagePagerState = PagerState(pageCount = { images.size })
    }

    Scaffold(
        bottomBar = {
            Row(modifier = Modifier
                .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${product.price} TL",
                    modifier = Modifier.weight(2f)
                )
                ElevatedButton(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.elevatedButtonColors()
                        .copy(containerColor = Color(0.2f, 0.6f, 0.2f, alpha = 1f)),
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
                        fontSize = 18.sp,
                        modifier = Modifier.padding(horizontal = 6.dp),
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
                        Icon(
                            Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                            contentDescription = "Back"
                        )
                    }
                    Box(modifier = Modifier.weight(1f))
                }
            }, colors = TopAppBarDefaults.topAppBarColors().copy(
                containerColor = Color.Transparent,
                scrolledContainerColor = MaterialTheme.colorScheme.background,
            ))
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(
                        top = it.calculateTopPadding() * 0,
                        bottom = it.calculateBottomPadding()
                    )
                    .verticalScroll(scrollState)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                        .background(color = MaterialTheme.colorScheme.secondary.copy(alpha = .4f))
                ) {
                    HorizontalPager(imagePagerState, modifier = Modifier.fillMaxSize()) { page ->
                        images.entries.toList()[page].value.let { image ->
                            BitmapFactory.decodeByteArray(image, 0, image.size)?.let { bitmap ->
                                Image(
                                    painter = BitmapPainter(bitmap.asImageBitmap()),
                                    contentDescription = "Loaded image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(8.dp)
                        .horizontalScroll(nameScrollState)
                )
                if (product.description.isNotEmpty()) {
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
                                text = product.description + product.description,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                    }
                }
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 100.dp),
                    modifier = Modifier
                        .padding(8.dp)
                        .wrapContentHeight()
                        .fillMaxWidth()
                ) {
                    items(categories.size) { index ->
                        Row(
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.tertiary,
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(start = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Absolute.SpaceAround
                        ) {
                            Text(
                                categories.entries.toList()[index].value,
                                fontSize = 16.sp, minLines = 1, maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.widthIn(min = 16.dp, max = 256.dp)
                            )
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun BuyProductScreenLightPreview() {
    /*val product = ProductDTO(
        id = "f882ebcf-f3d2-4677-b48c-283c23e36f05",
        name = "test data",
        description = "description",
        price = 123.459,
        categories = listOf("General"),
        images = listOf()
    )*/
    val product = Product(
        id = "f882ebcf-f3d2-4677-b48c-283c23e36f05",
        name = "test data",
        description = "description",
        price = 123.459
    )
    StockAppTheme(darkTheme = false) {
        BuyProductScreen(product = product, onNavigateToProductList = {})
    }
}