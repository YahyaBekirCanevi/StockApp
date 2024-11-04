package com.canevi.stockapp.ui.screen.productdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canevi.stockapp.model.Product
import com.canevi.stockapp.repository.ProductRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProductScreen(
    onBack: () -> Unit,
) {
    val productRepository = ProductRepository(LocalContext.current)
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var size by remember { mutableStateOf("") }
    var attributes by remember { mutableStateOf(mapOf<String, String>()) }
    var categories = remember { mutableStateListOf<String>() }

    var newCategory by remember { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)) {
                Column(modifier = Modifier.weight(1f).padding(16.dp)) {
                    CustomTextFieldWithLabel(label = "Product Name", value = name) { name = it }
                    CustomTextFieldWithLabel(label = "Quantity", value = quantity) {
                        quantity = it
                    }
                    CustomTextFieldWithLabel(label = "Description", value = description) {
                        description = it
                    }
                    CustomTextFieldWithLabel(label = "Size", value = size) { size = it }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Categories",
                            fontSize = 16.sp, color = Color.Gray, fontWeight = FontWeight.W700,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {
                            openDialog.value = true
                        }, modifier = Modifier.padding(horizontal = 8.dp)) {
                            Icon(Icons.Filled.Add, "Add Category", tint = Color.Gray)
                        }
                    }
                    Row(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .horizontalScroll(scrollState)
                    ) {
                        categories.map {
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
                                    it, fontSize = 16.sp, minLines = 1, maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.widthIn(min = 16.dp, max = 256.dp)
                                )
                                IconButton(onClick = { categories.remove(it) }) {
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
                                snackBarHostState.showSnackbar("Couldn't add new product!")
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
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 32.dp)
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

            if (openDialog.value) {
                BasicAlertDialog(
                    onDismissRequest = {
                        openDialog.value = false
                        newCategory = ""
                    }
                ) {
                    Surface(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight(),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                        ) {
                            CustomTextField(label = "Category", value = newCategory) {
                                newCategory = it
                            }
                            Button(
                                onClick = {
                                    categories.add(newCategory)
                                    openDialog.value = false
                                    newCategory = ""
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
                                Row {
                                    Icon(
                                        Icons.Default.Add,
                                        "Add Category",
                                        modifier = Modifier.padding(end = 8.dp),
                                        tint = MaterialTheme.colorScheme.inverseOnSurface
                                    )
                                    Text(
                                        "Add Category",
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.inverseOnSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun CustomTextFieldWithLabel(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Text(text = label, fontSize = 16.sp, color = Color.Gray, fontWeight = FontWeight.W700)
        CustomTextField(label = label, value = value) {
            onValueChange(it)
        }
    }
}

@Composable
fun CustomTextField(label: String = "", value: String, onValueChange: (String) -> Unit) {
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
            if (value.isEmpty() && !label.isEmpty()) {
                Text(text = "Enter $label", color = Color.Gray, fontSize = 18.sp)
            }
            innerTextField()
        },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
    )
}
