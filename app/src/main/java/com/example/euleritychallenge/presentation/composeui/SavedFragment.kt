package com.example.euleritychallenge.presentation.composeui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import coil.compose.rememberImagePainter
import com.example.euleritychallenge.MainActivity
import com.example.euleritychallenge.data.model.APIResponseItem
import com.example.euleritychallenge.presentation.viewmodel.PetsViewModel

class SavedPetsFragment : Fragment() {
    private lateinit var viewModel: PetsViewModel
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = (activity as MainActivity).viewModel
        navController = findNavController()

        return ComposeView(requireContext()).apply {
            setContent {
                SavedFragmentScreen(viewModel, navController)
            }
        }
    }
}

@Composable
fun SavedFragmentScreen(viewModel: PetsViewModel, navController: NavController) {
    viewModel.getSavedPets()
    val savedPetsList by viewModel.savedPets.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved Pets", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                if (savedPetsList.isEmpty()) {
                    // Display message if no pets are saved
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No Saved Pets", style = MaterialTheme.typography.h6)
                    }
                } else {
                    // Display the list of saved pets
                    LazyColumn {
                        items(savedPetsList) { pet ->
                            PetItem(
                                pet = pet,
                                onClick = {
                                    val action = pet.id?.let {
                                        SavedPetsFragmentDirections.actionSavedFragmentToComposeFragment2(
                                            pet.title,
                                            pet.description,
                                            pet.url,
                                            pet.created,
                                            it
                                        )
                                    }
                                    if (action != null) {
                                        navController.navigate(action)
                                    }
                                },
                                onDelete = {
                                    viewModel.deletePetFromDB(pet)
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}





@Composable
fun PetItem(pet: APIResponseItem, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        elevation = 8.dp
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberImagePainter(pet.url),
                contentDescription = "Pet Image",
                modifier = Modifier
                    .size(100.dp)
                    .padding(end = 16.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(pet.title, style = MaterialTheme.typography.h6)
                Text(pet.description, style = MaterialTheme.typography.body2)
                Text(
                    text = "Created on: ${pet.created}",
                    style = MaterialTheme.typography.caption,
                    color = Color.Gray
                )
            }

            // Add a delete button
            androidx.compose.material.IconButton(
                onClick = { onDelete() }
            ) {
                // You can use any icon you prefer
                androidx.compose.material.Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Pet"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSavedFragmentScreen() {
    val mockPets = listOf(
        APIResponseItem(
            id = 1,
            url = "https://example.com/pet1.jpg",
            title = "Pet 1",
            description = "Cute dog",
            created = "2024-09-05"
        ),
        APIResponseItem(
            id = 2,
            url = "https://example.com/pet2.jpg",
            title = "Pet 2",
            description = "Lovely cat",
            created = "2024-09-04"
        )
    )

    SavedFragmentScreenPreview(pets = mockPets)
}

@Composable
fun SavedFragmentScreenPreview(pets: List<APIResponseItem>) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved Pets", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                LazyColumn {
                    items(pets) { pet ->
                        PetItem(pet = pet, onClick = {
                            // Handle pet item click (mock for preview)
                        }, onDelete = {})
                    }
                }
            }
        }
    )
}



