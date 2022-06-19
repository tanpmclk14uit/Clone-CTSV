package com.example.svbookmarket.activities.data

import com.example.svbookmarket.activities.common.AppUtil
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.model.Order
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Named

class  OrderRepository @Inject constructor(
    @Named(Constants.USERS_REF) private val userCollRef: CollectionReference
) {

    private val TAG: String = "userOrder"

    fun getAllOrderFromCloudFireStore(): Query{
       return userCollRef.document(Firebase.auth.currentUser?.email.toString()).collection(TAG).orderBy(
            "dateTime",
            Query.Direction.DESCENDING
        )
    }

    fun updateOrderStatus(orderId: String){
        userCollRef.document(AppUtil.currentAccount.email).collection(TAG).document(orderId).update("status","CANCEL")
    }

    fun updateReason(orderId: String, reason: String){
        userCollRef.document(AppUtil.currentAccount.email).collection(TAG).document(orderId).update("reason",reason)
    }


}