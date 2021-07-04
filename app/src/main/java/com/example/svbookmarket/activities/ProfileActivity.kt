package com.example.svbookmarket.activities

import android.accounts.Account
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.model.AppAccount
import com.example.svbookmarket.activities.viewmodel.HomeViewModel
import com.example.svbookmarket.activities.viewmodel.UserViewModel
import com.example.svbookmarket.databinding.ActivityProfileBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint
class ProfileActivity() : AppCompatActivity() {

    lateinit var binding: ActivityProfileBinding
    val viewModel: UserViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBackButtonClick()

        setInfoView()
        setButtonEdit()

    }

    override fun onBackPressed() {
        startActivity(Intent(baseContext, UserManageActivity::class.java))
        finish()
    }

    @SuppressLint("SetTextI18n")
    private fun setInfoView(){
        binding.email.text = viewModel.getAccountInfo().email
        binding.userName.text = viewModel.getUserInfo().fullName
        binding.birthday.text = viewModel.getUserInfo().birthDay
        binding.addressLane.text = viewModel.getUserInfo().addressLane +", "+viewModel.getUserInfo().district
        binding.gender.text = viewModel.getUserInfo().gender
        binding.phone.text = viewModel.getUserInfo().phoneNumber
        binding.city.text = viewModel.getUserInfo().city
        if(viewModel.getUserInfo().gender =="Male"){
            binding.avatar.setImageResource(R.drawable.ic_male)
        }else{
            binding.avatar.setImageResource(R.drawable.ic_female)
        }
    }



    private  fun setBackButtonClick(){
        binding.imgBack.setOnClickListener {
            startActivity(Intent(baseContext, UserManageActivity::class.java))
            finish()
        }
    }
    private fun setButtonEdit(){
        binding.btnEdit.setOnClickListener {
            startActivity(Intent(baseContext, EditProfileActivity::class.java))
            finish()
        }
    }


}