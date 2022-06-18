package com.example.svbookmarket.activities.data

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.svbookmarket.activities.model.AppAccount
import com.example.svbookmarket.activities.model.Book
import com.google.firebase.firestore.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
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
                    val toast: Toast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
                    toast.setText("Thông báo đã được lưu")
                    toast.show()
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

    private suspend fun addNewCart(book: Book, user: AppAccount) {
        var dataBook: DocumentSnapshot =
            FirebaseFirestore.getInstance().collection("books").document(book.id!!).get().await()
        if (dataBook.data?.get("Name") != null) {
            var newCart: MutableMap<String, *> = mutableMapOf(
                "Image" to book.Image,
                "Name" to book.Name,
                "Saler" to book.Saler,
                "SalerName" to book.SalerName,
                "Description" to book.Description,
                "Kind" to book.Kind
            )
            FirebaseFirestore.getInstance().collection("accounts").document(user.email)
                .collection("userCart").document(book.id!!).set(newCart)
            Handler(Looper.getMainLooper()).post {
                val toast: Toast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
                toast.setText("Lưu thông báo thành công")
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

    fun deleteCart(user: AppAccount, id: String) {
        FirebaseFirestore.getInstance().collection("accounts").document(user.email)
            .collection("userCart").document(id).delete()
    }


    suspend fun clickBuy() {

    }

    init {
    }
}
