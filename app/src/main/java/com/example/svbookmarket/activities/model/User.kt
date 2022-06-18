package com.example.svbookmarket.activities.model

data class User(var fullName: String="",
                var gender: String="",
                var birthDay: String="",
                var studentId: String="",
                var career: String="",
                var studyClass: String="",
                var address: String= ""){
    fun isFullInformation(): Boolean{
        return gender.isNotBlank() && birthDay.isNotBlank() && career.isNotBlank() && studyClass.isNotBlank() && address.isNotBlank()
    }
}
