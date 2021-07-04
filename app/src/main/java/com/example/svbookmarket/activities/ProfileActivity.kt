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
import com.example.svbookmarket.activities.common.AppUtil
import com.example.svbookmarket.activities.model.AppAccount
import com.example.svbookmarket.activities.model.User
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
        loadData(AppUtil.currentSeller.email)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    private val db = Firebase.firestore
    private val dbAccountsReference = db.collection("salerAccount")

    private fun loadData(email: String) {
        dbAccountsReference.document(email).get()
            .addOnSuccessListener { result ->
                val userMap = result["user"] as HashMap<*, *>
                val recentUser: User = User(
                    fullName = userMap["fullName"].toString(),
                    gender = userMap["gender"].toString(),
                    birthDay = userMap["birthDay"].toString(),
                    phoneNumber = userMap["phoneNumber"].toString(),
                    addressLane = userMap["addressLane"].toString(),
                    city = userMap["city"].toString(),
                    district = userMap["district"].toString(),
                )
                AppUtil.currentSeller.user = recentUser
                setInfoView(AppUtil.currentSeller)
            }
    }



    @SuppressLint("SetTextI18n")
    private fun setInfoView(seller: AppAccount){
        binding.email.text = seller.email
        binding.userName.text = seller.user.fullName
        binding.birthday.text = seller.user.addressLane
        binding.addressLane.text = seller.user.district
        binding.phone.text = seller.user.phoneNumber
        binding.city.text = seller.user.city
        if(seller.user.gender =="Male"){
            binding.avatar.setImageResource(R.drawable.ic_male)
        }else{
            binding.avatar.setImageResource(R.drawable.ic_female)
        }
    }



    private  fun setBackButtonClick(){
        binding.imgBack.setOnClickListener {
            onBackPressed()
            finish()
        }
    }



}