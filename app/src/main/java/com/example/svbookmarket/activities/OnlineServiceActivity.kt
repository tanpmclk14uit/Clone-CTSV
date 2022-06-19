package com.example.svbookmarket.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.common.AppUtil
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.model.Order
import com.example.svbookmarket.activities.viewmodel.LoadDialog
import com.example.svbookmarket.activities.viewmodel.RegisterNewOnlineServiceOrderViewModel
import com.example.svbookmarket.databinding.ActivityOnlineServiceBinding
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Named

@AndroidEntryPoint
class OnlineServiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnlineServiceBinding
    private val viewModel: RegisterNewOnlineServiceOrderViewModel by viewModels()
    private lateinit var loadingDialog: LoadDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnlineServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.fragment)
        binding.bottomNavigationView.setupWithNavController(navController)
        loadingDialog = LoadDialog(this)
        onBackButtonClick()
        onRegisterClick()

    }

    private fun onBackButtonClick() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun onRegisterClick() {
        binding.register.setOnClickListener {
            val order = viewModel.makeOrder()
            if (order.isValidOrder()) {
                createNewUserOrder(order)
            } else {
                Toast.makeText(this, "Hãy chọn đầy đủ các thông tin", Toast.LENGTH_LONG).show()
            }

        }
    }

    private val TAG: String = "userOrder"

    private fun createNewUserOrder(order: Order) {
        loadingDialog.startLoading()
        FirebaseFirestore.getInstance().collection("accounts")
            .document(AppUtil.currentAccount.email).collection(TAG).document().set(order)
            .addOnCompleteListener {
                loadingDialog.dismissDialog()
            }.addOnSuccessListener {
                startActivity(Intent(this, ConfirmationActivity::class.java))
            }.addOnFailureListener {
                Toast.makeText(this, "Lỗi: $it", Toast.LENGTH_SHORT).show()
            }
    }
}