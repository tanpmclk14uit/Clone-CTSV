package com.example.svbookmarket.activities.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.data.AddressRepository
import com.example.svbookmarket.activities.data.BookRepository
import com.example.svbookmarket.activities.data.CartRepository
import com.example.svbookmarket.activities.model.Cart
import com.example.svbookmarket.activities.model.UserDeliverAddress
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class CheckoutViewModel @Inject constructor( private val cartRepository: CartRepository,
                                            private val bookRepository: BookRepository,
                                            private val addressRepository: AddressRepository): ViewModel() {

    private var _checkoutItem = MutableLiveData<MutableList<Cart>>()
    val checkoutItem get() = _checkoutItem
    private var _deliveryAddress = MutableLiveData<UserDeliverAddress>()
    val deliverAddress get() = _deliveryAddress
    init {
        loadCheckoutData()
        loadDeliverAddress()
      }

    private fun loadCheckoutData()
    {
        cartRepository.getCart(CurrentUserInfo.getInstance().currentProfile).addSnapshotListener(object :
            EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.w(Constants.VMTAG, "Listen failed.", error)
                    return
                }

                val checkoutList: MutableList<Cart> = ArrayList()
                for (doc in value!!) {
                    if (doc.data["isChose"].toString() == "true") {
                        var item = Cart(
                            "",
                            doc.data["image"].toString(),
                            doc.data["title"].toString(),
                            doc.data["author"].toString(),
                            doc.data["Quantity"].toString().toDouble().roundToInt(),
                            doc.data["price"].toString().toLong(),
                            doc.data["saler"].toString(),
                            doc.data["salerName"].toString(),
                            true
                        )
                        item.id = doc.id
                        checkoutList.add(item)
                    }
                }
                checkoutItem.value = checkoutList
            }

        })
    }

    private fun loadDeliverAddress()
    {
        addressRepository.getChosenAddress(CurrentUserInfo.getInstance().currentProfile).addSnapshotListener(object :
        EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.w(Constants.VMTAG, "Listen failed.", error)
                    return
                }

                val itemDeliverAddress: UserDeliverAddress
                for (doc in value!!) {
                    if (doc.data["chose"].toString() == "true") {
                        var item = UserDeliverAddress(
                            doc.id,
                            doc.data["fullName"].toString(),
                            doc.data["phoneNumber"].toString(),
                            doc.data["addressLane"].toString(),
                            doc.data["district"].toString(),
                            doc.data["city"].toString(),
                            true
                        )
                        itemDeliverAddress = item
                        deliverAddress.value =  itemDeliverAddress
                        break
                    }
                }

            }

        })
    }
}