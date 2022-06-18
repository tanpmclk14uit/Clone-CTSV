package com.example.svbookmarket.activities.data

import android.util.Log
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.model.Book
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*

public class FullBookList private constructor(var lstFullBook: MutableList<Book> = mutableListOf()) {

    init {
        getDataBySnapshot()
    }

    private fun getDataBySnapshot() {
        var ref = FirebaseFirestore.getInstance().collection("books")
        ref.addSnapshotListener (object :
            EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.w(Constants.VMTAG, "Listen failed.", error)
                    return
                }
                val bookList: MutableList<Book> = ArrayList()
                for (doc in value!!) {

                   try{
                       if (doc.data["Name"] != null) {
                           var bookItem = doc.toObject(Book::class.java)
                           bookItem.id = doc.id
                           bookList.add(bookItem)
                       }
                   }catch (e:Exception){
                       Log.i("VMTAG", "$doc")
                   }

                }
                lstFullBook = bookList
            }
        })
    }
    private object Holder {
        val INSTANCE = FullBookList()
    }

    companion object {

        fun getInstance(): FullBookList {
            return Holder.INSTANCE
        }


    }
}