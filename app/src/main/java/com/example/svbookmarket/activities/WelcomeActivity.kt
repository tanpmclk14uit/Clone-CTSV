package com.example.svbookmarket.activities

import CurrentUserInfo
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.common.AppUtil
import com.example.svbookmarket.activities.model.AppAccount
import com.example.svbookmarket.activities.model.User
import com.example.svbookmarket.activities.viewmodel.LoadDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WelcomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private val dbAccountsReference = db.collection("accounts")
    var loadDialog = LoadDialog(this)
    var isBackPressedOnce = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        auth = Firebase.auth

        var login: Button = findViewById(R.id.btnLogin)
        login.setOnClickListener {
            startActivity(Intent(baseContext, LoginActivity::class.java))
            finish();
        }
    }


    public override fun onBackPressed() {
        if (isBackPressedOnce) {
            super.onBackPressed()
            return
        }

        this.isBackPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { isBackPressedOnce = false }, 2000)
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            loadDialog.startLoading()
            loadData(currentUser.email.toString())
        }
    }

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
                AppUtil.currentUser = recentUser
                AppUtil.currentAccount = AppAccount(
                    result["email"].toString(),
                    result["password"].toString(),
                    recentUser
                )
                CurrentUserInfo.getInstance()
                startActivity(Intent(baseContext, HomeActivity::class.java))
                loadDialog.dismissDialog()
                finish()
            }
    }
}