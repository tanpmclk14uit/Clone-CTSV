package com.example.svbookmarket.activities

import CurrentUserInfo
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.common.AppUtil
import com.example.svbookmarket.activities.model.AppAccount
import com.example.svbookmarket.activities.model.User
import com.example.svbookmarket.activities.viewmodel.LoadDialog
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    //init view
    private lateinit var loginEmailLayout: TextInputLayout
    private lateinit var loginPasswordLayout: TextInputLayout
    private lateinit var loginEmail: EditText
    private lateinit var loginPassword: EditText
    private lateinit var loginSignUp: TextView
    private lateinit var loginButton: Button


    //init database reference
    private val db = Firebase.firestore
    private val dbAccountsReference = db.collection("accounts")
    private lateinit var auth: FirebaseAuth
    private lateinit var loadDialog: LoadDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
        loginEmailLayout = findViewById(R.id.LoginEmailLayout)
        loginEmail = findViewById(R.id.LoginEmail)
        loginPasswordLayout = findViewById(R.id.LoginPasswordLayout)
        loginPassword = findViewById(R.id.LoginPassword)
        loginSignUp = findViewById(R.id.LoginSignUp)
        loginButton = findViewById(R.id.loginButton)
        val forgotPassword: TextView = findViewById(R.id.forgotPassword)

        forgotPassword.setOnClickListener {
            startActivity(Intent(baseContext, ForgotPassword::class.java))
            finish()
        }


        loginButton.setOnClickListener {
            onButtonLoginClick()
        }

        val imgBackArrow: AppCompatImageView = findViewById(R.id.imgBackArrow)
        imgBackArrow.setOnClickListener {
            startActivity(Intent(baseContext, WelcomeActivity::class.java))
        }
        loginSignUp.setOnClickListener {
            startActivity(Intent(baseContext, RegisterActivity::class.java))
            finish() //remove activity from backstack
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(baseContext, WelcomeActivity::class.java))
    }

    private fun onButtonLoginClick() {

        if (isValidEmail() && isValidPassword()) {
            loadDialog= LoadDialog(this)
            loadDialog.startLoading()
            dbAccountsReference.get().addOnSuccessListener { result ->
                var emailExist = false
                for (document in result) {
                    if (document.id == loginEmail.text.toString()) {
                        emailExist = true
                    }
                }
                if (emailExist) {
                    val email = loginEmail.text.toString()
                    val password = loginPassword.text.toString()
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                loadData(email)
                                loadDialog.dismissDialog()
                            } else {
                                loginPasswordLayout.error = task.exception!!.message.toString()
                                loadDialog.dismissDialog()
                            }
                        }

                } else {
                    loginEmailLayout.error = "Email is not exist, sign up now?"
                    loadDialog.dismissDialog()
                }
            }
        }
    }


    private fun loadData(email: String) {
        dbAccountsReference.document(email).get()
            .addOnSuccessListener { result ->
                val userMap = result["user"] as HashMap<*, *>
                val recentUser = User(
                    fullName = userMap["fullName"].toString(),
                    gender = userMap["gender"].toString(),
                    birthDay = userMap["birthDay"].toString(),
                    studentId = userMap["studentId"].toString(),
                    career = userMap["career"].toString(),
                    studyClass = userMap["studyClass"].toString(),
                    address = userMap["address"].toString(),
                )
                AppUtil.currentUser = recentUser
                AppUtil.currentAccount = AppAccount(
                    result["email"].toString(),
                    result["password"].toString(),
                    recentUser
                )
                CurrentUserInfo.destroyOld()
                startActivity(Intent(baseContext, HomeActivity::class.java))
                finish()
            }
        }



    private fun isEmailRightFormat(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }

    private fun isValidPassword(): Boolean {

        return if (loginPassword.text.isEmpty()) {
            loginPasswordLayout.error = "Password can not empty"
            false
        } else {
            if (loginPassword.text.count() < 8) {
                loginPasswordLayout.error = "Password must have more than 8 character"
                false
            } else {
                loginPasswordLayout.error = null
                true
            }
        }
    }

    private fun isValidEmail(): Boolean {
        return if (loginEmail.text.isEmpty()) {
            loginEmailLayout.error = "Email can not empty"
            false
        } else {
            if (isEmailRightFormat(loginEmail.text.toString().trim())) {
                loginEmailLayout.error = null
                true
            } else {
                loginEmailLayout.error = "Invalid email"
                false
            }
        }
    }
}