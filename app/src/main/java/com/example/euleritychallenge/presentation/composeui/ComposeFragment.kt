package com.example.euleritychallenge.presentation.composeui

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.euleritychallenge.MainActivity
import com.example.euleritychallenge.R
import com.example.euleritychallenge.data.model.APIResponseItem
import com.example.euleritychallenge.presentation.viewmodel.PetsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random

class ComposeFragment : Fragment() {

    private lateinit var viewModel: PetsViewModel

    private val args: ComposeFragmentArgs by navArgs()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = (activity as MainActivity).viewModel
        return ComposeView(requireContext()).apply {
            setContent {
                MyComposeScreen(viewModel, args.petTitle, args.petUrl, args.petDescription, args.petCreatedDate, args.petId)
            }
        }
    }
}

@Composable
fun MyComposeScreen(
    viewModel: PetsViewModel,
    petTitle: String,
    imageUrl: String,
    description: String,
    createdDate: String,
    id: Int?
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val savePetStatus by viewModel.savePetStatus.observeAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = petTitle,
            style = MaterialTheme.typography.caption.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .weight(1f)
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFD2E3FC))
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .placeholder(R.drawable.ic_image_placeholder)
                        .error(R.drawable.ic_image_placeholder)
                        .build()
                )

                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .size(200.dp)
                        .padding(16.dp)

                )
            }
        }



        Button(
            onClick = {
                coroutineScope.launch {
                    val success = downloadImage(imageUrl, context)
                    if (success) {
                        Toast.makeText(context, "Image downloaded successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to download Image", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Download Image")
        }

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .weight(1f)
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFFD9D5C)) // Apply custom color here
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "DESCRIPTION:",
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    text = description,
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = "CREATED DATE:",
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$createdDate",
                    style = MaterialTheme.typography.body1,
                )
            }

            if (id == null || id == 0) {
                Box(
                    modifier = Modifier.fillMaxSize()// This makes the Box fill the entire available space
                ) {
                    FloatingActionButton(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.savePet(APIResponseItem(
                                    id = generateUniqueRandomId(viewModel),
                                    title = petTitle,
                                    description = description,
                                    url = imageUrl,
                                    created = createdDate
                                ))
                            }
                        },
                        backgroundColor = Color.Black,
                        contentColor = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Save")
                    }

                    LaunchedEffect(savePetStatus) {
                        savePetStatus?.let {
                            val message = if (it.success) "Item saved successfully" else "Item already saved"
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            viewModel.clearSavePetStatus()
                        }
                    }
                }
            }
        }
    }
}

suspend fun downloadImage(imageUrl: String, context: Context): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val uri = Uri.parse(imageUrl)
            var bitmap: Bitmap? = null

            if (imageUrl.startsWith("content://")) {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    bitmap = BitmapFactory.decodeStream(inputStream)
                } ?: return@withContext false
            } else {

                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream: InputStream = connection.inputStream
                    bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream.close()
                } else {
                    return@withContext false
                }
            }

            if (bitmap != null) {

                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, "downloaded_image.jpg")
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                val uriToSave: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

                uriToSave?.let { imageUri ->
                    context.contentResolver.openOutputStream(imageUri)?.use { outputStream ->
                        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    }
                }

                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}

private fun generateUniqueRandomId(viewModel: PetsViewModel): Int {
    var id: Int
    do {
        id = Random.nextInt(1, Int.MAX_VALUE) // Generate a random positive integer
    } while (isIdInDatabase(id, viewModel))
    return id
}

private fun isIdInDatabase(id: Int, viewModel: PetsViewModel): Boolean {
    return viewModel.isIdExistsInDb(id)
}



/*
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun MyComposeScreenPreview() {
    MyComposeScreen(
        petTitle = "Sample Pet Title",
        imageUrl = "https://via.placeholder.com/200",
        description = "This is a sample description of the pet.",
        createdDate = "2024-09-04"
    )
}*/
