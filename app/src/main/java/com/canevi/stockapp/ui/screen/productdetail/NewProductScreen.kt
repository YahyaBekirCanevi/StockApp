package com.canevi.stockapp.ui.screen.productdetail

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Clear
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
import com.canevi.stockapp.model.dto.ImageDTO
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

    val categoryRepository = CategoryRepository(currentContext)
    val productRepository = ProductRepository(currentContext)

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
    val openCategoryDialog = remember { mutableStateOf(false) }
    val imageDialog = remember { mutableStateOf<BitmapPainter?>(null) }
    var exitWithoutSaveDialog by remember { mutableStateOf(false) }


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

    fun saveProduct() {
        val product = Product(
            name = name,
            description = description,
            price = price.toDouble()
        )

        coroutineScope.launch {
            val newProduct = productRepository.addProduct(product)
            if (newProduct == null) {
                snackBarHostState.showSnackbar("Couldn't add new product!")
                return@launch
            }
            val addImagesResponse = productRepository.addImagesToProduct(
                newProduct.id!!,
                images.map { ImageDTO.ofProduct(newProduct.id, it) }
            )
            if (addImagesResponse.isEmpty()) {
                snackBarHostState.showSnackbar("Couldn't add images to product! Try again in your profile.")
                return@launch
            }
            val addCategoryResponse = productRepository.addCategoriesToProduct(
                newProduct.id,
                categories
            )
            if (addCategoryResponse.isEmpty()) {
                snackBarHostState.showSnackbar("Couldn't add categories to product! Try again in your profile.")
                return@launch
            }
        }
    }

    fun isEmpty() {
        exitWithoutSaveDialog = !(name.isEmpty() && description.isEmpty() &&
                price.isEmpty() && categories.isEmpty() && images.isEmpty())
        if (!exitWithoutSaveDialog) onBack()
    }

    BackHandler {
        isEmpty()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { isEmpty() }) {
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
                                .background(MaterialTheme.colorScheme.surface)
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
                                    val painter = BitmapPainter(bitmap.asImageBitmap())
                                    Box(Modifier.clickable(onClick = {
                                        imageDialog.value = painter
                                    })) {
                                        Image(
                                            painter = painter,
                                            contentDescription = "Loaded image",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                        Box(
                                            Modifier
                                                .padding(8.dp)
                                                .size(20.dp)
                                                .align(Alignment.TopEnd)
                                                .clickable(onClick = {
                                                    images.remove(image)
                                                })
                                                .background(Color.Gray, RoundedCornerShape(50.dp))
                                                .padding(2.dp)
                                        ) {
                                            Icon(Icons.Filled.Clear, "", tint = Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    CustomTextFieldWithLabel(label = "Product Name", value = name) { name = it }
                    CustomTextFieldWithLabel(label = "Price", value = price, keyboardType = KeyboardType.Decimal) {
                        price = it.replace(',', '.')
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
                            openCategoryDialog.value = true
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
                    onClick = { saveProduct() },
                    enabled = name.isNotEmpty() && price.isNotEmpty() && description.isNotEmpty()
                            && categories.isNotEmpty() && images.isNotEmpty(),
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

            if (openCategoryDialog.value) {
                BasicAlertDialog(
                    onDismissRequest = {
                        openCategoryDialog.value = false
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
            if (imageDialog.value != null) {
                BasicAlertDialog(
                    onDismissRequest = {
                        imageDialog.value = null
                    }
                ) {
                    Image(
                        painter = imageDialog.value!!,
                        contentDescription = "Loaded image",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
            if (exitWithoutSaveDialog) {
                BasicAlertDialog(
                    onDismissRequest = {
                        exitWithoutSaveDialog = false
                    }
                ) {
                    Column(Modifier.background(MaterialTheme.colorScheme.surface).padding(8.dp)) {
                        Text(
                            "Exiting Without Saving",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.W700,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp).fillMaxWidth()
                        )
                        Text(
                            "Are you sure to exit without saving?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp).padding(horizontal = 32.dp).fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            Button(
                                onClick = { onBack() },
                                modifier = Modifier.padding(start = 8.dp).weight(1f),
                                colors = ButtonColors(
                                    containerColor = Color.Red.copy(alpha = .6f),
                                    contentColor = MaterialTheme.colorScheme.onSurface,
                                    disabledContainerColor = Color.Red.copy(alpha = .6f),
                                    disabledContentColor = MaterialTheme.colorScheme.onSurface,
                                )
                            ) {
                                Text("Don't Save")
                            }
                            Button(
                                onClick = { exitWithoutSaveDialog = false },
                                modifier = Modifier.padding(horizontal = 8.dp).weight(1f),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                colors = ButtonColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.primary,
                                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                                    disabledContentColor = MaterialTheme.colorScheme.primary,
                                )
                            ) {
                                Text("Cancel")
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
