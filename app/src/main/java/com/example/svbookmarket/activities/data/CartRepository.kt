package com.example.svbookmarket.activities.data

import CurrentUserInfo
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.svbookmarket.activities.model.AppAccount
import com.example.svbookmarket.activities.model.Book
import com.example.svbookmarket.activities.model.Cart
import com.google.firebase.firestore.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import com.example.svbookmarket.activities.model.UserDeliverAddress as MyAddress


@Singleton
class CartRepository @Inject constructor(
    @ApplicationContext val context: Context,
) {



    suspend fun addCart(book: Book, user: AppAccount) {
        val rootRef = FirebaseFirestore.getInstance()
        val docIdRef = rootRef.collection("accounts").document(user.email).collection("userCart")
            .document(book.id!!)

        docIdRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document: DocumentSnapshot? = task.result
                if (document!!.exists()) {
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            addOldCart(book, user)
                        }
                    }
                } else {
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            addNewCart(book, user)
                        }
                    }
                }
            }
        }
    }

    private suspend fun addOldCart(book: Book, user: AppAccount) {
        var avaiBook: Double = -1.0
        var currenOnCart: Double = -1.0
        var bookData: DocumentSnapshot = FirebaseFirestore.getInstance().collection("books").document(book.id!!).get().await()
        if (bookData.data?.get("Name") != null) {
            var currentNumInCart: DocumentSnapshot =
                FirebaseFirestore.getInstance().collection("accounts")
                    .document(user.email).collection("userCart").document(book.id!!).get().await()

            avaiBook = bookData.data?.get("Counts").toString().toDouble()
            currenOnCart = currentNumInCart.data?.get("Quantity").toString().toDouble()

            if (avaiBook > currenOnCart && avaiBook != -1.0 && currenOnCart != -1.0 && avaiBook != 0.0) {
                FirebaseFirestore.getInstance().collection("accounts").document(user.email)
                    .collection("userCart").document(book.id!!)
                    .update("Quantity", FieldValue.increment(1))

                Handler(Looper.getMainLooper()).post {
                    val toast: Toast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
                    toast.setText("Add to cart success")
                    toast.show()
                    val handler = Handler()
                    handler.postDelayed({ toast.cancel() }, 500)
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    val toast: Toast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
                    toast.setText("At max in store")
                    toast.show()
                    val handler = Handler()
                    handler.postDelayed({ toast.cancel() }, 500)
                }
            }
        }
    }

    private suspend fun addNewCart(book: Book, user: AppAccount) {
        var avaiBook: Double = -1.0
        var dataBook: DocumentSnapshot = FirebaseFirestore.getInstance().collection("books").document(book.id!!).get().await()
        if (dataBook.data?.get("Name") != null) {
            avaiBook = dataBook.data?.get("Counts").toString().toDouble()
            if (avaiBook != -1.0 && avaiBook != 0.0) {
                var newCart: MutableMap<String, *> = mutableMapOf(
                    "Quantity" to 1,
                    "author" to book.Author,
                    "image" to book.Image,
                    "title" to book.Name,
                    "isChose" to false,
                    "price" to book.Price,
                    "saler" to book.Saler,
                    "salerName" to book.SalerName,
                )
                FirebaseFirestore.getInstance().collection("accounts").document(user.email)
                    .collection("userCart").document(book.id!!).set(newCart)
                Handler(Looper.getMainLooper()).post {
                    val toast: Toast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
                    toast.setText("Add to cart success")
                    toast.show()
                    val handler = Handler()
                    handler.postDelayed({ toast.cancel() }, 500)
                }
            } else {
                val toast: Toast = Toast.makeText(context, "messenger", Toast.LENGTH_SHORT)
                toast.setText("At max in store")
                toast.show()
                val handler = Handler()
                handler.postDelayed({ toast.cancel() }, 500)
            }
        }
    }


    fun getCart(user: AppAccount): Query {
        return FirebaseFirestore.getInstance().collection("accounts").document(user.email)
            .collection("userCart")
    }

    suspend fun newQuantityForItem(id: String, plusOrMinus: Boolean, user: AppAccount) {

        var avaiBook: Double = -1.0
        var currenOnCart: Double = -1.0

        val currentListBook = FullBookList.getInstance().lstFullBook
        val currentListCart = CurrentUserInfo.getInstance().lstUserCart

        for (i in 0 until  currentListBook.size)
        {
            if (currentListBook[i].id == id)
            {
                avaiBook = currentListBook[i].Counts.toDouble()
            }
        }

        for (i in 0 until  currentListCart.size)
        {
            if (currentListCart[i].id == id)
            {
                currenOnCart = currentListCart[i].numbers.toDouble()
            }
        }


            if (currenOnCart < avaiBook && plusOrMinus && avaiBook != -1.0 && currenOnCart != -1.0) {
                FirebaseFirestore.getInstance().collection("accounts").document(user.email)
                    .collection("userCart").document(id).update("Quantity", FieldValue.increment(1))
            }
            else if (currenOnCart > 0 && !plusOrMinus && avaiBook != -1.0 && currenOnCart != -1.0 && currenOnCart<= avaiBook) {
                FirebaseFirestore.getInstance().collection("accounts").document(user.email)
                    .collection("userCart").document(id)
                    .update("Quantity", FieldValue.increment(-1))
                currenOnCart -= 1
                if (currenOnCart == 0.0) {
                    deleteCart(user, id)
                }
            } else if (currenOnCart == avaiBook && avaiBook != -1.0 && currenOnCart != -1.0) {
                Handler(Looper.getMainLooper()).post {
                    val toast: Toast = Toast.makeText(context,"messenger",Toast.LENGTH_SHORT)
                    toast.setText("At max in store")
                    toast.show()
                    val handler = Handler()
                    handler.postDelayed({ toast.cancel() }, 500)
                }
            }
                else if (currenOnCart > avaiBook && avaiBook != -1.0 && currenOnCart != -1.0)
            {
                FirebaseFirestore.getInstance().collection("accounts").document(user.email)
                    .collection("userCart").document(id)
                    .update("Quantity", avaiBook)
                }
    }

    fun deleteCart(user: AppAccount, id: String) {
        FirebaseFirestore.getInstance().collection("accounts").document(user.email)
            .collection("userCart").document(id).delete()
    }

    fun isChoseChange(user: AppAccount, id: String, isChose: Boolean) {
        FirebaseFirestore.getInstance().collection("accounts").document(user.email)
            .collection("userCart").document(id).update("isChose", isChose)
    }

    suspend fun moveToUserOrDer(user: AppAccount, listNeedToMove: MutableList<Cart>, deliverAddress: MyAddress)
    {
        val mapOfAddress = hashMapOf<String, Any>("addressId" to deliverAddress.id, "addressLane" to deliverAddress.addressLane, "city" to deliverAddress.city,
            "district" to deliverAddress.district,"fullName" to deliverAddress.fullName,"phoneNumber" to deliverAddress.phoneNumber, "userId" to user.email)
       val newOrderId : String = FirebaseFirestore.getInstance().collection("accounts").document(user.email)
            .collection("userOrder").add(mapOfAddress).await().get().await().id

        for (i in 0 until listNeedToMove.size) {

            val mapOfOrder = hashMapOf<String, Any>("Quantity" to listNeedToMove[i].numbers, "author" to listNeedToMove[i].author, "image" to listNeedToMove[i].imgUrl,
                "price" to listNeedToMove[i].price,"title" to listNeedToMove[i].name)
            FirebaseFirestore.getInstance().collection("accounts").document(user.email)
                .collection("userOrder").document(newOrderId).collection("books").document(listNeedToMove[i].id).set(mapOfAddress)
        }
    }

    suspend fun clickBuy()
    {

    }

    init {
    }
}
