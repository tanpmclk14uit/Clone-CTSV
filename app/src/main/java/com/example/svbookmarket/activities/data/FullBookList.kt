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
                           Log.i("VMTAG", ("${bookItem.Name} ${bookItem.Price}"))
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

//        private fun getDataFromDb(): MutableList<Book> {
//            val lstFullBook: MutableList<Book> = mutableListOf()
//            val db: FirebaseFirestore = FirebaseFirestore.getInstance()
//            val ref = db.collection("books")
//                .get()
//                .addOnSuccessListener { result ->
//                    for (document in result) {
//                        val doc = document.data;
//                        val authors: String = document.get("Author").toString();
//                        val count: Long = document.get("Counts").toString().toLong();
//                        val description: String = document.get("Description").toString();
//                        val image: Uri = Uri.parse(document.get("Image").toString());
//                        val kind: String = document.get("Kind").toString();
//                        val name: String = document.get("Name").toString();
//                        val rate: Double = document.get("rate").toString().toDouble();
//                        val price: Long = document.get("Price").toString().toLong();
//                        val book: Book = Book(
//                            document.id,
//                            image,
//                            name,
//                            authors,
//                            price,
//                            rate,
//                            kind,
//                            count,
//                            description
//                        )
//                        lstFullBook.add(book)
//                    }
//                }
//
//            return lstFullBook
//        }

    }
}