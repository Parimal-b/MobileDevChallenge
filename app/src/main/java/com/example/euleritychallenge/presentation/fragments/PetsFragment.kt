package com.example.euleritychallenge.presentation.fragments

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.euleritychallenge.MainActivity
import com.example.euleritychallenge.R
import com.example.euleritychallenge.data.model.APIResponseItem
import com.example.euleritychallenge.data.util.Resource
import com.example.euleritychallenge.databinding.DialogAddPetBinding
import com.example.euleritychallenge.databinding.FragmentPetsBinding
import com.example.euleritychallenge.presentation.adapter.PetsAdapter
import com.example.euleritychallenge.presentation.viewmodel.PetsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.random.Random


class PetsFragment : Fragment() {
    private lateinit var viewModel: PetsViewModel
    private lateinit var petsAdapter: PetsAdapter
    private lateinit var fragmentPetsBinding: FragmentPetsBinding
    private var selectedImageUri: Uri? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentPetsBinding = FragmentPetsBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel
        initRecyclerView()
        viewPetsList()
        setSortButtonListener()
        setupSearchView()
        setUpAddPetButton()

    }

    private fun viewPetsList(){
        viewModel.getCombinedPets()
        viewModel.pets.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {

                    hideProgressBar()
                    response.data?.let {
                        petsAdapter.differ.submitList(it)

                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(activity, "An error occurred : $it", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }

                else -> {}
            }
        }
    }


    private fun setSortButtonListener(){
        fragmentPetsBinding.sortImageView.setOnClickListener {
            if(viewModel.isSortAscending){
                fragmentPetsBinding.sortImageView.setImageResource(R.drawable.ic_sort_by_desc)
            }else{
                fragmentPetsBinding.sortImageView.setImageResource(R.drawable.ic_sort_by_asc)
            }
            viewModel.isSortAscending = !viewModel.isSortAscending
            viewPetsList()
            scrollToTopAfterLayout()
        }
    }

    private fun initRecyclerView(){
        petsAdapter = PetsAdapter { pet ->
            val action = PetsFragmentDirections.actionPetsFragmentToComposeFragment(
                pet.title,
                pet.description,
                pet.url,
                pet.created,
                pet.id ?: 0
            )
            findNavController().navigate(action)
        }
        fragmentPetsBinding.rvPets.apply {
            adapter = petsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun showProgressBar(){
        fragmentPetsBinding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        fragmentPetsBinding.progressBar.visibility = View.INVISIBLE
    }

    private fun scrollToTopAfterLayout() {
        fragmentPetsBinding.rvPets.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View?,
                left: Int, top: Int, right: Int, bottom: Int,
                oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int
            ) {
                fragmentPetsBinding.rvPets.removeOnLayoutChangeListener(this)
                fragmentPetsBinding.rvPets.scrollToPosition(0)
            }
        })
    }

    private fun setupSearchView() {
        fragmentPetsBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText ?: "")
                return true
            }
        })
    }

    private fun filterList(query: String) {
        val filteredList = viewModel.pets.value?.data?.filter {
            it.title.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true)
        }
        petsAdapter.differ.submitList(filteredList)
    }

    private fun setUpAddPetButton() {
        fragmentPetsBinding.fabAddPet.setOnClickListener {
            showAddPetDialog()
        }
    }

    private var dialogView: View? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            // Show the selected image in the dialog
            val imageView = dialogView?.findViewById<ImageView>(R.id.ivPetImage)
            imageView?.setImageURI(it)
        }
    }

    private fun showAddPetDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(context)
        dialogView = inflater.inflate(R.layout.dialog_add_pet, null)
        val binding = DialogAddPetBinding.bind(dialogView!!)

        builder.setView(binding.root)
            .setTitle("Add New Pet")
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Create") { dialog, _ ->
                val title = binding.etTitle.text.toString().trim()
                val description = binding.etDescription.text.toString().trim()
                val imageUri = selectedImageUri.toString()

                if (title.isNotEmpty() && description.isNotEmpty() && selectedImageUri != null) {
                    val newPet = APIResponseItem(generateUniqueRandomId(),getCurrentFormattedDate(), description, title, imageUri)
                    viewModel.savePet(newPet)
                    Toast.makeText(context, "Pet added successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "All fields must be filled out.", Toast.LENGTH_SHORT).show()
                }
            }

        binding.ivPetImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        builder.create().show()
    }

    fun getCurrentFormattedDate(): String {
        val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(Date())
    }

    private fun generateUniqueRandomId(): Int {
        var id: Int
        do {
            id = Random.nextInt(1, Int.MAX_VALUE) // Generate a random positive integer
        } while (isIdInDatabase(id))
        return id
    }

    private fun isIdInDatabase(id: Int): Boolean {
        return viewModel.isIdExistsInDb(id)
    }
}