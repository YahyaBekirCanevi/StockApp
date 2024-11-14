package com.canevi.stockapp.ui.screen.productdetail

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canevi.stockapp.model.Category
import com.canevi.stockapp.model.Product
import com.canevi.stockapp.repository.CategoryRepository
import com.canevi.stockapp.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProductScreen(
    onBack: () -> Unit,
) {
    val currentContext = LocalContext.current

    val productRepository = ProductRepository(currentContext)
    val categoryRepository = CategoryRepository(currentContext)

    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var categories = remember { mutableStateListOf<String>() }
    val images = remember { mutableStateListOf<ByteArray>() }
    val categorySearch = remember { mutableStateListOf<Category>() }


    val imagesScrollState = rememberScrollState()
    var newCategory by remember { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }


    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        it?.let {
            coroutineScope.launch {
                val bitmap = withContext(Dispatchers.IO) {
                    val source = ImageDecoder.createSource(currentContext.contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                }
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()
                images.add(byteArray)
            }
        }
    }

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
                Column(modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)) {

                    Row(
                        Modifier
                            .horizontalScroll(imagesScrollState)
                            .padding(bottom = 16.dp)) {
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
                                )
                                .clickable(onClick = {
                                    launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                }),
                                contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Add\nPhoto",
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                            )
                        }
                        images.map { image ->
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
                    CustomTextFieldWithLabel(label = "Product Name", value = name) { name = it }
                    CustomTextFieldWithLabel(label = "Price", value = price, keyboardType = KeyboardType.Decimal) {
                        price = it.replace('.', ',')
                    }
                    CustomTextFieldWithLabel(label = "Description", value = description) {
                        description = it
                    }
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
                            Icon(Icons.Filled.Add, "Add Category", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
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
                                    categories[index], fontSize = 16.sp, minLines = 1, maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.widthIn(min = 16.dp, max = 256.dp)
                                )
                                IconButton(onClick = { categories.removeAt(index) }) {
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
                        val product = Product(
                            name = name,
                            description = description,
                            price = price.toDouble(),
                            categoryIdList = categories
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
                            MaterialTheme.colorScheme.primary,
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
                            .wrapContentWidth(),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                        ) {
                            BasicTextField(
                                value = newCategory,
                                onValueChange = {
                                    newCategory = it
                                    coroutineScope.launch {
                                        val categoriesSearched = categoryRepository.searchCategories(name=newCategory)
                                        categorySearch.clear()
                                        categorySearch.addAll(categoriesSearched)
                                    }
                                },
                                textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                singleLine = true,
                                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                                decorationBox = { innerTextField ->
                                    if (newCategory.isEmpty()) {
                                        Text(text = "Enter Category", color = Color.Gray, fontSize = 18.sp)
                                    }
                                    innerTextField()
                                },
                                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                            )
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(minSize = 100.dp),
                                modifier = Modifier.padding(8.dp)
                            ) {
                                items(categories.size) { index ->
                                    Box(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .background(
                                                MaterialTheme.colorScheme.secondary,
                                                RoundedCornerShape(16.dp)
                                            )
                                            .padding(horizontal = 12.dp, vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = categories[index],
                                            color = MaterialTheme.colorScheme.onSurface,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                            categorySearch.map {
                                ListItem(
                                    headlineContent = { Text(it.name) },
                                    modifier = Modifier.clickable(onClick = {
                                        categories.add(it.name)
                                        newCategory = ""
                                        categorySearch.clear()
                                    })
                                )
                            }
                            Button(
                                onClick = {
                                    categories.add(newCategory)
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
                                        MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clip(RoundedCornerShape(16.dp))
                            ) {
                                Row {
                                    Icon(
                                        Icons.Default.Add,
                                        "Add Category",
                                        modifier = Modifier.padding(end = 8.dp),
                                        tint = MaterialTheme.colorScheme.inverseOnSurface,
                                    )
                                    Text(
                                        "Add Category",
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.inverseOnSurface,
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
fun CustomTextFieldWithLabel(label: String, value: String, keyboardType: KeyboardType = KeyboardType.Unspecified, onValueChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = label, fontSize = 16.sp, color = Color.Gray, fontWeight = FontWeight.W700)
        CustomTextField(label = label, value = value, keyboardType = keyboardType) {
            onValueChange(it)
        }
    }
}

@Composable
fun CustomTextField(label: String = "", value: String, keyboardType: KeyboardType = KeyboardType.Unspecified, onValueChange: (String) -> Unit) {
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
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next, keyboardType = keyboardType)
    )
}
