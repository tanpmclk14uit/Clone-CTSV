package com.example.svbookmarket.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.common.AppUtil
import com.example.svbookmarket.activities.model.Information
import com.example.svbookmarket.databinding.ActivityContactBinding
import com.google.firebase.firestore.FirebaseFirestore

class ContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setInfoView()
        binding.idBack.setOnClickListener {
            onBackPressed()
        }

    }
    private fun setInfoView(){
        FirebaseFirestore.getInstance().collection("salerAccount")
            .document("admin.ctsv@gm.uit.edu.vn").get().addOnSuccessListener {
                val mapToInformation = it.data!!["information"] as Map<*,*>
                val information: Information = Information(
                    fullName = mapToInformation["fullName"].toString(),
                    introduction = mapToInformation["introduction"].toString(),
                    webSite = mapToInformation["webSite"].toString(),
                    phoneNumber = mapToInformation["phoneNumber"].toString(),
                    address = mapToInformation["address"].toString(),
                )
                binding.email.text = "admin.ctsv@gm.uit.edu.vn"
                binding.userName.text = information.fullName
                binding.website.text = information.webSite
                binding.address.text = information.address
                binding.phone.text = information.phoneNumber
                binding.introduction.text = information.introduction
            }
    }

}