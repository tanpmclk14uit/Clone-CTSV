package com.example.svbookmarket.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.viewmodel.UserViewModel
import com.example.svbookmarket.databinding.ActivityUserManageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserManageActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivityUserManageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserManageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setButtonBack()
        setLogout()

        setUpUserInfoView()
        setAllOrderClick()
        setWaitingForDeliverRy()
        setSavedNotificationClick()
        setYourProfile()
        binding.contact.setOnClickListener {
            startActivity(Intent(this, ContactActivity::class.java))
        }
    }

    private fun setSavedNotificationClick(){
        binding.savedNotification.setOnClickListener{
            startActivity(Intent(baseContext, CartActivity::class.java))
        }
    }
    private fun setAllOrderClick() {
        binding.allOrders.setOnClickListener {
            startActivity(Intent(baseContext, UserOrder::class.java))
        }
    }

    private fun setWaitingForDeliverRy(){
        binding.waitingForDelivery.setOnClickListener {
            startActivity(Intent(baseContext, DeliveringActivity::class.java))
        }
    }

    private fun setButtonBack() {
        binding.backButton.setOnClickListener {
            onBackPressed()
            finish()
        }
    }

    private fun setYourProfile() {
        binding.yourInfo.setOnClickListener {
            startActivity(Intent(baseContext, ProfileActivity::class.java))
        }
    }

    private lateinit var auth: FirebaseAuth
    private fun setLogout() {
        binding.logout.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(baseContext, WelcomeActivity::class.java))
            finish()
        }
    }

    private fun setUpUserInfoView() {
        if (userViewModel.getUserInfo().gender == "Male") {
            binding.avatar.setImageResource(R.drawable.ic_male)
        } else {
            binding.avatar.setImageResource(R.drawable.ic_female)
        }
        binding.userName.text = userViewModel.getUserInfo().fullName
        binding.userEmail.text = userViewModel.getAccountInfo().email
    }


}