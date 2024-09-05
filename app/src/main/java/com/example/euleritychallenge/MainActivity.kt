package com.example.euleritychallenge

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.euleritychallenge.databinding.ActivityMainBinding
import com.example.euleritychallenge.presentation.adapter.PetsAdapter
import com.example.euleritychallenge.presentation.viewmodel.PetsViewModel
import com.example.euleritychallenge.presentation.viewmodel.PetsViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var factory: PetsViewModelFactory
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: PetsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this,factory)
            .get(PetsViewModel::class.java)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        binding.bnvPets.setupWithNavController(
            navHostFragment.findNavController()
        )
    }
}