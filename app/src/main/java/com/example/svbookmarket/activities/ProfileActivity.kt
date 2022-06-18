package com.example.svbookmarket.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.common.AppUtil
import com.example.svbookmarket.activities.model.AppAccount
import com.example.svbookmarket.activities.model.User
import com.example.svbookmarket.activities.viewmodel.UserViewModel
import com.example.svbookmarket.databinding.ActivityProfileBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity() : AppCompatActivity() {

    lateinit var binding: ActivityProfileBinding
    val viewModel: UserViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBackButtonClick()
        setEditButtonClick()
        loadData(AppUtil.currentAccount.email)
    }
    private fun setEditButtonClick(){
        binding.editButton.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
            finish()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private val db = Firebase.firestore
    private val dbAccountsReference = db.collection("accounts")

    private fun loadData(email: String) {
        dbAccountsReference.document(email).get()
            .addOnSuccessListener { result ->
                val userMap = result["user"] as HashMap<*, *>
                val recentUser: User = User(
                    fullName = userMap["fullName"].toString(),
                    gender = userMap["gender"].toString(),
                    birthDay = userMap["birthDay"].toString(),
                    studentId = userMap["studentId"].toString(),
                    career = userMap["career"].toString(),
                    studyClass = userMap["studyClass"].toString(),
                    address = userMap["address"].toString(),
                )
                AppUtil.currentAccount.user = recentUser
                setInfoView(AppUtil.currentAccount)
            }
    }


    @SuppressLint("SetTextI18n")
    private fun setInfoView(user: AppAccount){
        binding.email.text = user.email
        binding.userName.text = user.user.fullName
        binding.address.text = user.user.address
        binding.birthDay.text = user.user.birthDay
        binding.studentId.text = user.user.studentId
        binding.studentClass.text = user.user.studyClass
        binding.career.text = user.user.career
        if(user.user.gender =="Female"){
            binding.avatar.setImageResource(R.drawable.ic_female)
        }else{
            binding.avatar.setImageResource(R.drawable.ic_male)
        }
        if(user.user.isFullInformation()){
            binding.editButton.text = "Chỉnh sửa thông tin cá nhân"
        }else{
            binding.editButton.text = "Bổ sung thông tin cá nhân"
        }
    }

    private  fun setBackButtonClick(){
        binding.imgBack.setOnClickListener {
            onBackPressed()
            finish()
        }
    }



}