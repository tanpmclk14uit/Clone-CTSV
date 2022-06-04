package com.example.svbookmarket.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.svbookmarket.R
import com.example.svbookmarket.databinding.ActivityDeliveringBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeliveringActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeliveringBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDeliveringBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.fragment)
        binding.bottomNavigationView.setupWithNavController(navController)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }
}