package com.example.dieter.ui.screen.add.ingredients

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.dieter.DieterAppState
import com.example.dieter.R
import com.example.dieter.application.DieterApplication
import com.example.dieter.data.source.domain.IngredientModel
import com.example.dieter.ui.component.CameraPreview
import com.example.dieter.ui.component.MeasurementDropdown
import com.example.dieter.ui.component.UpButton
import com.example.dieter.ui.theme.DieterShapes
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

private const val PERMISSIONS_REQUEST_CODE = 100
private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)

fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
    ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
}

@Composable
fun AddIngredientsScreen(
    viewModel: AddIngredientsViewModel,
    goUp: () -> Unit = {},
    navigateToSearchIngredient: () -> Unit = {},
    calculateNutrients: () -> Unit = {},
    appState: DieterAppState
) {
    val context = LocalContext.current
    if (!hasPermissions(context)) {
        requestPermissions(
            context as Activity,
            PERMISSIONS_REQUIRED,
            PERMISSIONS_REQUEST_CODE
        )
    }

    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        val imageCapture by remember { mutableStateOf(ImageCapture.Builder().build()) }
        val ingredientState by appState.ingredientsState.collectAsState()
        Box {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxSize()
            ) {
                /**
                 * TODO Camera keep printing error logs that makes it hard to read other logs
                 * Disabling it for a while until there's a solution
                 */
                CameraPreview(
                    imageCapture = imageCapture,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(384.dp)
                )
                // SimpleCameraPreview(
                // imageCapture = imageCapture,
                // modifier = Modifier
                //     .fillMaxWidth()
                //     .height(384.dp)
                // )
                // Spacer(
                //     modifier = Modifier
                //         .fillMaxWidth()
                //         .height(384.dp)
                // )
                Spacer(Modifier.size(12.dp))
                Text(
                    "Take a picture to detect the\n" +
                        "ingredient or add manually",
                    style = MaterialTheme.typography.h6
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    ingredientState.forEach {
                        IngredientCard(
                            ingredientModel = it,
                            remove = { appState.removeIngredient(it.key) },
                            onPortionUpdated = { p -> appState.updatePortion(it.key, p) }
                        )
                    }
                    Spacer(Modifier.size(136.dp))
                }
            }
            UpButton {
                goUp()
                appState.clearIngredient()
            }
        }
        BottomBar(
            takePicture = {
                takePhoto(imageCapture) {
                    appState.photoUri = it
                }
            },
            searchIngredient = navigateToSearchIngredient,
            next = calculateNutrients,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 36.dp)
        )
    }
}

private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
private const val PHOTO_EXTENSION = ".jpg"
private fun takePhoto(imageCapture: ImageCapture, onTaken: (Uri) -> Unit) {
    Log.d("takePhoto", "takePhoto: Called")
    // TODO: Remove bang operator
    val context = DieterApplication.applicationContext()!!
    val photoFile = createFile(getOutputDirectory(context), FILENAME, PHOTO_EXTENSION)

    // Create output options object which contains file + metadata
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    // Set up image capture listener, which is triggered after photo has
    // been taken
    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                Log.e("Take Photo", "Photo capture failed: ${exc.message}", exc)
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                val msg = "Photo capture succeeded: $savedUri"
                onTaken(savedUri)
                Log.d("Take Photo", msg)
            }
        }
    )
}

private fun createFile(baseFolder: File, format: String, extension: String) =
    File(
        baseFolder,
        SimpleDateFormat(format, Locale.US)
            .format(System.currentTimeMillis()) + extension
    )

/** Use external media if it is available, our app's file directory otherwise */
fun getOutputDirectory(context: Context): File {
    val appContext = context.applicationContext
    val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
        File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists())
        mediaDir else appContext.filesDir
}

@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    searchIngredient: () -> Unit = {},
    takePicture: () -> Unit = {},
    next: () -> Unit = {}
) {
    Surface(
        elevation = 8.dp,
        shape = DieterShapes.medium,
        border = BorderStroke(2.dp, MaterialTheme.colors.primary),
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .background(MaterialTheme.colors.background)
        ) {
            IconButton(
                onClick = searchIngredient
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "search"
                )
            }
            IconButton(
                onClick = takePicture
            ) {
                Spacer(
                    modifier = Modifier
                        .size(48.dp)
                        .border(2.dp, MaterialTheme.colors.primary, CircleShape)
                )
            }
            IconButton(
                onClick = next
            ) {
                Icon(
                    imageVector = Icons.Filled.NavigateNext,
                    contentDescription = "next"
                )
            }
        }
    }
}

@Composable
private fun IngredientCard(
    ingredientModel: Map.Entry<IngredientModel, Int>,
    remove: () -> Unit = {},
    onPortionUpdated: (Int) -> Unit = {}
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {

            IconButton(onClick = { remove() }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "delete"
                )
            }
            Text(ingredientModel.key.label, modifier = Modifier.padding(end = 16.dp))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(2.dp, MaterialTheme.colors.primary, DieterShapes.small),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            MeasurementDropdown(
                measurements = ingredientModel.key.measures,
                modifier = Modifier
                    .padding(start = 8.dp, bottom = 8.dp, end = 8.dp)
                    .width(214.dp)
            )

            OutlinedTextField(
                value = ingredientModel.value.toString(),
                onValueChange = {
                    onPortionUpdated(it.toIntOrNull() ?: 0)
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .padding(start = 8.dp, bottom = 8.dp, end = 8.dp)
                    .width(112.dp)
            )
        }
    }
}

// viewBlock = { context: Context ->
//     val view =
//         LayoutInflater.from(context)
//             .inflate(R.layout.my_layout, null, false)
//
//     val textView = view.findViewById<TextView>(R.id.text)
//     // do whatever you want...
//     view // return the viw
// },
// update = { view ->
//     // Update view
// }

// @Composable
// private fun SimpleCameraPreview() {
//     val lifecycleOwner = LocalLifecycleOwner.current
//     val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
//     AndroidView(
//         factory = { context: Context ->
//             val view =
//                 LayoutInflater.from(context)
//                     .inflate(R.layout.camera_host, null, false)
//
//             // do whatever you want...
//             view // return the viw
//         }
//     ) {
//         cameraProviderFuture.addListener(
//             {
//                 val cameraProvider = cameraProviderFuture.get()
//                 bindPreview(
//                     lifecycleOwner,
//                     it as PreviewView /*the inflated layout*/,
//                     cameraProvider
//                 )
//             },
//             ContextCompat.getMainExecutor(context)
//         )
//     }
// }

fun bindPreview(
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    cameraProvider: ProcessCameraProvider
) {
    var preview: Preview = Preview.Builder().build()

    var cameraSelector: CameraSelector = CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()

    preview.setSurfaceProvider(previewView.surfaceProvider)

    var camera = cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
}

// @Preview
// @Composable
// private fun BottomBarPreview() {
//     DieterTheme {
//         BottomBar()
//     }
// }
//
// @Preview
// @Composable
// private fun IngredientCardPreview() {
//     DieterTheme {
//         IngredientCard(
//             mapOf(
//                 IngredientModel(
//                     ")",
//                     "Broccoli",
//                     0f,
//                     mapOf(
//                         NutrientType.ENERC_KCAL to 9f,
//                         NutrientType.FIBTG to 10f,
//                         NutrientType.CA to 2f,
//                         NutrientType.FAT to 39f,
//                         NutrientType.P to 45f
//                     ),
//                     "Meal",
//                     "Meal Label",
//                     emptyList(),
//                     null
//                 ) to 1
//             ).entries.first()
//         )
//     }
// }
