package com.example.svbookmarket.activities.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.data.BookRepository
import com.example.svbookmarket.activities.data.CartRepository
import com.example.svbookmarket.activities.model.Cart
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckoutDialogViewModel @Inject constructor( private  val cartRepository: CartRepository,
                                                   private val bookRepository: BookRepository): ViewModel(){

    private var _billingItem = MutableLiveData<MutableList<Cart>>()
    val billingItem get() = _billingItem


    init {
         loadData()
     }

    private fun loadData()
    {
        cartRepository.getCart(CurrentUserInfo.getInstance().currentProfile).addSnapshotListener(object :
            EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.w(Constants.VMTAG, "Listen failed.", error)
                    return
                }

                val billingList: MutableList<Cart> = ArrayList()
                for (doc in value!!) {
                    if (doc.data["isChose"].toString() == "true") {
                        var item = Cart(
                            "",
                            doc.data["image"].toString(),
                            doc.data["title"].toString(),
                            doc.data["author"].toString(),
                            doc.data["Quantity"].toString().toInt(),
                            doc.data["price"].toString().toLong(),
                            doc.data["saler"].toString(),
                            doc.data["salerName"].toString(),
                            true
                        )
                        item.id = doc.id
                        billingList.add(item)
                    }
                }
                billingItem.value = billingList
            }
        })
    }
}