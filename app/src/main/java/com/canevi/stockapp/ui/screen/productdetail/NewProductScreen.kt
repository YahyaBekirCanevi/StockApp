package com.canevi.stockapp.ui.screen.productdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canevi.stockapp.model.Product
import com.canevi.stockapp.model.dto.DetailedProductDTO
import com.canevi.stockapp.repository.ProductRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProductScreen(
    onNavigateToProductList: () -> Unit,
) {
    val productRepository = ProductRepository(LocalContext.current)
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var size by remember { mutableStateOf("") }
    var attributes by remember { mutableStateOf(mapOf<String, String>()) }
    var categories by remember { mutableStateOf("") }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    IconButton(onClick = { onNavigateToProductList() }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    CustomTextField(label = "Product Name", value = name) { name = it }
                    CustomTextField(label = "Quantity", value = quantity) { quantity = it }
                    CustomTextField(label = "Description", value = description) { description = it }
                    CustomTextField(label = "Size", value = size) { size = it }
                    CustomTextField(label = "Categories (comma separated)", value = categories) { categories = it }
                }

                Button(
                    onClick = {
                        /*val productDTO = DetailedProductDTO(
                            id = "",  // Set appropriate ID if necessary
                            name = name,
                            quantity = quantity.toIntOrNull() ?: 0,
                            description = if (description.isNotBlank()) description else null,
                            size = if (size.isNotBlank()) size else null,
                            attributes = attributes,  // Update if you allow user to input attributes
                            categories = categories.split(",").map { it.trim() }
                        )*/
                        val product = Product(
                            name = name,
                            quantity = quantity.toIntOrNull() ?: 0
                        )

                        coroutineScope.launch {
                            val newProduct = productRepository
                                .addProduct(product)
                            if (newProduct == null) {
                                snackBarHostState.showSnackbar( "Couldn't add new product!")
                                return@launch
                            }
                        }
                    },
                    colors = ButtonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        contentColor = Color.Black,
                        disabledContentColor = Color.Black
                    ),
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 0.dp)
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.tertiary
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Text(
                        "Save Product",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    )
                }
            }
        }
    )
}

@Composable
fun CustomTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(text = "Enter $label", color = Color.Gray, fontSize = 16.sp)
                }
                innerTextField()
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )
    }
}
