package com.example.svbookmarket.activities.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.svbookmarket.activities.common.Constants.VMTAG
import com.example.svbookmarket.activities.data.BookRepository
import com.example.svbookmarket.activities.model.Book
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class FeatureViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    @ApplicationContext val context: Context
) : ViewModel() {

    private var _books = MutableLiveData<MutableList<Book>>()
    val books get() = _books

    private var _top5Books = MutableLiveData<List<Book>>()
    val top5 get() = _top5Books

    private var _top10Books = MutableLiveData<List<Book>>()
    val top10 get() = _top10Books

    fun getBookFrom(): MutableLiveData<MutableList<Book>> {
        bookRepository.getBooksFromCloudFirestore1().addSnapshotListener(object :
            EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.w(VMTAG, "Listen failed.", error)
                    return
                }

                val bookList: MutableList<Book> = ArrayList()
                for (doc in value!!) {
                    var bookItem = doc.toObject(Book::class.java)
                    bookList.add(bookItem)

                }
                books.value = bookList
            }

        })
        setTop5()
        setTop10()
        return books
    }


    fun setTop5() {
        _top5Books.value = _books.value?.take(5)
        _books.value?.drop(5)
    }

    fun setTop10() {
        _top5Books.value = _books.value?.take(10)
        _books.value?.drop(10)

    }

    init {
        getBookFrom()
    }
}