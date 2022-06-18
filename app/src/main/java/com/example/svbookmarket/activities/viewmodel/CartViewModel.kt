package com.example.svbookmarket.activities.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.data.CartRepository
import com.example.svbookmarket.activities.model.Book
import com.google.firebase.firestore.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class CartViewModel @Inject constructor(private val cartRepository: CartRepository) :ViewModel() {
    private var _cartItem = MutableLiveData<MutableList<Book>>()
    val cartItem get() = _cartItem


    init{
        loadCartItem()
        onDeleteHandle()
    }

    fun loadCartItem()
    {
        cartRepository.getCart(CurrentUserInfo.getInstance().currentProfile).addSnapshotListener (object :
            EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.w(Constants.VMTAG, "Listen failed.", error)
                    return
                }

                val cartList: MutableList<Book> = ArrayList()
                for (doc in value!!) {
                    var bookItem = doc.toObject(Book::class.java)
                    bookItem.id = doc.id
                    cartList.add(bookItem)
                }
                cartItem.value = cartList
            }
        })
    }



    fun deleteCart(id: String)
    {
        cartRepository.deleteCart(CurrentUserInfo.getInstance().currentProfile, id)
    }

    fun onDeleteHandle()
    {
        FirebaseFirestore.getInstance().collection("books").addSnapshotListener { snapshots, e ->
            e?.let {
                Log.d("0000000000", e.message!!)
            }

                for(doc in snapshots!!.documentChanges) {
                    when(doc.type)
                    {
                        DocumentChange.Type.REMOVED -> {
                            deleteCart(doc.document.id)
                        }
                    }
                }
        }
    }
}