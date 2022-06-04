package com.example.svbookmarket.activities.data

import com.example.svbookmarket.activities.common.AppUtil
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.model.User
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject
import javax.inject.Named

class UserRepository @Inject constructor(
    @Named(Constants.USERS_REF) private val userCollRef: CollectionReference
) {
    var user: User = User()

    fun loadData() {
       user = AppUtil.currentUser
    }
    fun updateUserData(user: User){
       AppUtil.currentAccount.user = user
        AppUtil.currentUser = user
        userCollRef.document(AppUtil.currentAccount.email).set(AppUtil.currentAccount)
    }


}

