package com.example.svbookmarket.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.model.User
import com.example.svbookmarket.activities.viewmodel.UserViewModel
import com.example.svbookmarket.databinding.ActivityEditProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.regex.Pattern

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {
    val viewModel: UserViewModel by viewModels()
    lateinit var binding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        genderSetUp()
        setData()
        setBirthDateSelector()
        setSaveButtonCommand()
        setBackButton()
    }

    private fun setBackButton(){
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }
    private fun setBirthDateSelector(){
        binding.edtBirthday.setOnClickListener {
            pickDateSetting()
        }
    }

    private fun setSaveButtonCommand(){
        binding.btnSaveProfile.setOnClickListener {
            if(isNotEmptyInformation()){
                if(isValidName()) {
                    val user: User = User(
                        fullName = binding.edtName.text.toString(),
                        gender = binding.gender.text.toString(),
                        birthDay = binding.edtBirthday.text.toString(),
                        career = binding.edtCareer.text.toString(),
                        studyClass = binding.edtStudentClass.text.toString(),
                        address = binding.edtAddress.text.toString(),
                        studentId = viewModel.getUserInfo().studentId
                    )
                    viewModel.updateUserInfo(user)
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(baseContext, ProfileActivity::class.java))
                    finish()
                }
            }else{
                Toast.makeText(this, "Tất cả thông tin không được trống!", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun isNotEmptyInformation(): Boolean{
        return binding.edtName.text.isNotBlank() &&
        binding.edtBirthday.text.isNotBlank() &&
        binding.edtCareer.text.isNotBlank() &&
        binding.edtStudentClass.text.isNotBlank() &&
        binding.edtAddress.text.isNotBlank() &&
        binding.gender.text.isNotBlank()
    }

    private fun isValidName(): Boolean {
        return if (binding.edtName.text.isEmpty()) {
            binding.edtName.error = "Name can not empty"
            false
        } else {
            return if (isNameContainNumberOrSpecialCharacter(binding.edtName.text.toString())) {
                binding.edtName.error = "Name can not contain number of special character"
                false
            } else {
                binding.edtName.error = null
                true
            }
        }
    }
    private fun isNameContainNumberOrSpecialCharacter(name: String): Boolean {
        var hasNumber: Boolean = Pattern.compile(
            "[0-9]"
        ).matcher(name).find()
        var hasSpecialCharacter: Boolean = Pattern.compile(
            "[!@#$%&.,\"':;?*()_+=|<>?{}\\[\\]~-]"
        ).matcher(name).find()
        return hasNumber || hasSpecialCharacter
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    private fun genderSetUp(){
        val adapter = ArrayAdapter(
            this,
            R.layout.gender_item,
            resources.getStringArray(R.array.gender)
        )
        binding.gender.setAdapter(adapter)
    }
    private fun setData(){
        val user =  viewModel.getUserInfo()
        binding.edtName.setText( user.fullName)
        binding.edtBirthday.setText(user.birthDay)
        binding.edtCareer.setText(user.career)
        binding.edtStudentClass.setText(user.studyClass)
        binding.edtAddress.setText(user.address)
        binding.gender.setText(viewModel.getUserInfo().gender, false)

    }
    private fun pickDateSetting(){
        val c: Calendar = Calendar.getInstance()
        val day: Int = c.get(Calendar.DAY_OF_MONTH)
        val month: Int = c.get(Calendar.MONTH)
        val year: Int = c.get(Calendar.YEAR)

        val dpd: DatePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _: DatePicker, mYear: Int, mMonth: Int, mDay: Int ->

                binding.edtBirthday.setText("$mDay/${mMonth + 1}/$mYear")
            },
            year,
            month,
            day
        )
        dpd.show()
    }
}