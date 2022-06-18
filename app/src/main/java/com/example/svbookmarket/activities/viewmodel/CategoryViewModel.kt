package com.example.svbookmarket.activities.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.data.BookRepository
import com.example.svbookmarket.activities.model.Book
import com.example.svbookmarket.activities.model.Category
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private var _category = MutableLiveData<MutableList<Category>>()
    val category get() = _category


    private var _books = MutableLiveData<MutableList<Book>>()
    val books get() = _books

    var isBookLoaded = MutableLiveData<Boolean>()

    fun getBookFrom(): MutableLiveData<MutableList<Book>> {
        bookRepository.getBooksFromCloudFirestore1().addSnapshotListener(object :
            EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.w(Constants.VMTAG, "Listen failed.", error)
                    return
                }
                val bookList: MutableList<Book> = ArrayList()
                for (doc in value!!) {
                    val bookItem = doc.toObject(Book::class.java)
                    bookList.add(bookItem)
                }
                books.value = bookList
                isBookLoaded.value = true
            }
        })
        return books
    }
    private fun loadCategoryList() {
        _category.value = mutableListOf(
            Category(R.drawable.img_art, Constants.CATEGORY.CHUNG),
            Category(R.drawable.img_fiction, Constants.CATEGORY.HV),
            Category(R.drawable.img_comic, Constants.CATEGORY.NB),
            Category(R.drawable.img_novel, Constants.CATEGORY.PH),
            Category(R.drawable.img_business, Constants.CATEGORY.LTCQ),
            Category(R.drawable.img_tech, Constants.CATEGORY.VB2),
        )
    }

    fun getBooksOfCategory(categoryName: String): ArrayList<Book> {
        val filted = _books.value?.filter { category ->
            category.Kind == categoryName
        }
        return if (filted != null) ArrayList(filted) else ArrayList(mutableListOf())
    }


    init {
        getBookFrom()
        loadCategoryList()
        isBookLoaded.value = false
    }
}