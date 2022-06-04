package com.example.svbookmarket.activities.data

import com.example.svbookmarket.activities.common.AppUtil
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.model.AppAccount
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject
import javax.inject.Named

class AccountRepository@Inject constructor(
    @Named(Constants.USERS_REF) private val accountColRef: CollectionReference
) {
    var account = AppAccount()

    fun loadData() {
        account = AppUtil.currentAccount
    }

}