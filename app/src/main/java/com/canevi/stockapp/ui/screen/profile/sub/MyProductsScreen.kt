package com.canevi.stockapp.ui.screen.profile.sub

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canevi.stockapp.model.Product
import com.canevi.stockapp.model.dto.ImageDTO
import com.canevi.stockapp.model.dto.ProductDTO
import com.canevi.stockapp.repository.ProductDetailRepository
import com.canevi.stockapp.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MyProductsScreen() {
    val coroutineScope = rememberCoroutineScope()
    val productRepository = ProductRepository(LocalContext.current)
    val products = remember { mutableStateListOf<Product>() }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val productList = withContext(Dispatchers.IO) {
                productRepository.getProducts()
            }
            products.addAll(productList)
        }
    }

    Column {
        products.filterNot { it.id == null }.map { ProductItem(it) }
    }
}

@Composable
fun ProductItem(product: Product) {
    val productDetailRepository = ProductDetailRepository(LocalContext.current)
    val (productDetail, setProductDetail) = remember { mutableStateOf<ProductDTO?>(null) }

    LaunchedEffect(Unit) {
        setProductDetail(productDetailRepository.readProduct(product.id!!))
    }

    if (productDetail == null)
        Row(
            Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 8.dp)
                .background(
                    MaterialTheme.colorScheme.onSurface.copy(alpha = .2f), RoundedCornerShape(16.dp)
                )
        ) {
            Text(product.name, color = MaterialTheme.colorScheme.onSurface)
        }
    else
        Row(
            Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 8.dp)
                .background(
                    MaterialTheme.colorScheme.onSurface.copy(alpha = .2f), RoundedCornerShape(16.dp)
                )
        ) {
            if (productDetail.images.isNotEmpty())
                Box(
                    Modifier
                        .size(120.dp)
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.tertiary)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    ImageDTO.decode(productDetail.images.first().imageData).let { image ->
                        BitmapFactory.decodeByteArray(image, 0, image.size)?.let { bitmap ->
                            Image(
                                painter = BitmapPainter(bitmap.asImageBitmap()),
                                contentDescription = "Loaded image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.FillWidth
                            )
                            Box(
                                Modifier
                                    .padding(8.dp)
                                    .size(20.dp)
                                    .align(Alignment.TopEnd)
                                    .clickable(onClick = {
                                        //productDetail.images.remove(image)
                                    })
                                    .background(Color.Gray, RoundedCornerShape(50.dp))
                                    .padding(2.dp)
                            ) {
                                Icon(Icons.Filled.Clear, "", tint = Color.White)
                            }
                        }
                    }
                }
            Column(Modifier.padding(start = 16.dp), verticalArrangement = Arrangement.Center) {
                Text(
                    productDetail.name, color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
                if (productDetail.categories.isNotEmpty())
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 100.dp),
                        modifier = Modifier
                            .padding(8.dp)
                            .wrapContentHeight()
                            .fillMaxWidth()
                    ) {
                        items(productDetail.categories.size % 3) { index ->
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
                                    productDetail.categories[index],
                                    fontSize = 16.sp,
                                    minLines = 1,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.widthIn(min = 16.dp, max = 256.dp)
                                )
                                IconButton(onClick = {
                                    //productDetail.categories.removeAt(index)
                                }) {
                                    Icon(
                                        Icons.Default.Close,
                                        "Remove",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
            }
        }
}