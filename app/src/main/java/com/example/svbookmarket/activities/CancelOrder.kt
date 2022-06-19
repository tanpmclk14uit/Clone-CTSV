package com.example.svbookmarket.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatImageView
import com.example.svbookmarket.activities.common.AppUtil
import com.example.svbookmarket.activities.viewmodel.CancelOrderViewModel
import com.example.svbookmarket.databinding.ActivityCancelOrderBinding
import com.example.svbookmarket.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CancelOrder : AppCompatActivity() {

    private lateinit var binding: ActivityCancelOrderBinding

    private val viewModel: CancelOrderViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCancelOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imgBackArrow: ImageButton = findViewById(R.id.backButton)
        imgBackArrow.setOnClickListener {
            onBackPressed()
            finish()
        }

        binding.submit.setOnClickListener {
            updateData()
        }

    }
    private fun updateData(){
        if(binding.LoginEmail.text.isNotEmpty()){
            viewModel.updateData(AppUtil.currentOrder, binding.LoginEmail.text.toString())
            startActivity(Intent(baseContext,CancelConfirmation::class.java))
            finish()
        }else{
            binding.LoginEmailLayout.error = "Bắt buộc nhập lý do!"
        }

    }
}