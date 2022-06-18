package com.example.svbookmarket.activities.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.common.Constants.VMTAG
import com.example.svbookmarket.activities.data.AdsRepository
import com.example.svbookmarket.activities.data.BookRepository
import com.example.svbookmarket.activities.data.CartRepository
import com.example.svbookmarket.activities.data.CategoryRepository
import com.example.svbookmarket.activities.model.AppAccount
import com.example.svbookmarket.activities.model.Book
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    adsRepository: AdsRepository,
    categoryRepository: CategoryRepository,
    private val cartRepository: CartRepository
) : ViewModel() {
    private var _books = MutableLiveData<MutableList<Book>>()
    val books get() = _books
    private var _carts = MutableLiveData<MutableList<Book>>()
    val carts get() = _carts


    fun getCart(user: AppAccount): MutableLiveData<MutableList<Book>> {
        cartRepository.getCart(user).addSnapshotListener { value, error ->
            if (error != null) {
                Log.w(Constants.VMTAG, "Listen failed.", error)
            } else {
                val cartList: MutableList<Book> = ArrayList()
                for (doc in value!!) {
                    val bookItem = Book(id = System.currentTimeMillis().toString())
                    cartList.add(bookItem)
                }
                carts.value = cartList
            }
        }
        return carts
    }
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
                       val bookItem = doc.toObject(Book::class.java)
                       bookItem.id = doc.id
                       bookList.add(bookItem)
                }
                books.value = bookList
            }

        })
        Log.d(VMTAG, "getbookcalled")

        return books
    }

    private var _ads = adsRepository.ads
    val ads get() = _ads

    private var _category = categoryRepository.category
    val category get() = _category

    fun getBooksOfCategory(categoryName: String): ArrayList<Book> {
        Log.d("CATEGORY", categoryName)
        Log.d("CATEGORY",  _books.value?.get(0)?.Kind.toString())
        val filted = _books.value?.filter { category ->
            category.Kind.equals(categoryName)
        }
        return ArrayList(filted)
    }

    init {
        Log.i("sharedvm", "sharedViewmodel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("SHARED_VM", "shared vm cleared!")
    }
}