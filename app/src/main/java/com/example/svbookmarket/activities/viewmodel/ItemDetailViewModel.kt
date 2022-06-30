package com.example.svbookmarket.activities.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.common.Constants.ITEM
import com.example.svbookmarket.activities.data.BookRepository
import com.example.svbookmarket.activities.data.CartRepository
import com.example.svbookmarket.activities.data.CommentRepository
import com.example.svbookmarket.activities.model.Book
import com.example.svbookmarket.activities.model.Comment
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor (private val savedStateHandle: SavedStateHandle,
                                               private val cartRepository: CartRepository,
                                               private val commentRepository: CommentRepository,
                                               private  val bookRepository: BookRepository) : ViewModel() {
    private var _books = MutableLiveData<MutableList<Book>>()
    val books get() = _books

    private var _itemToDisplay = MutableLiveData<Book>()
    val itemToDisplay get() = _itemToDisplay

    init {
        loadBooks()
        loadItem()
    }
    private val db = Firebase.firestore

    private fun loadBooks() {
        val des =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut egestas in ligula a maximus. Mauris venenatis, neque vitae sollicitudin dapibus, dolor lorem tempor orci, eget viverra ante eros non sem. Nam maximus dignissim purus, ac cursus felis. Duis maximus odio nunc, nec dapibus ligula ultricies non. Aliquam efficitur sapien ut nisl aliquet, eget malesuada urna egestas. Nullam sed orci urna. Praesent iaculis dapibus urna, at rutrum ipsum aliquet at. Pellentesque pellentesque augue vel tortor convallis aliquam. Etiam porttitor id urna at dictum. Donec scelerisque auctor quam, id varius ligula. Aliquam eget nibh et urna dapibus vestibulum. Donec mauris ipsum, aliquet ut risus ac, efficitur porta tortor. Donec ac libero ut leo lobortis elementum. Nunc commodo metus nunc.\nPhasellus iaculis nisi a leo vehicula sodales. Fusce hendrerit quam eget tortor semper ultrices. Vivamus rhoncus molestie massa et volutpat. Proin cursus ex ac diam ornare consectetur. Curabitur vitae congue lectus. Pellentesque a purus fermentum, varius est vel, faucibus risus. Nullam vitae massa vitae diam fermentum condimentum vitae a sem. Etiam eu lorem a libero sollicitudin placerat.\nDuis sodales imperdiet quam, vitae laoreet ex fermentum non. Vivamus convallis magna in justo laoreet, a ornare enim sollicitudin. Nam laoreet neque eu tempus commodo. In sollicitudin enim sit amet rutrum posuere. Integer mattis aliquet posuere. Integer blandit mauris vel erat molestie faucibus. Ut non urna in urna interdum venenatis non sit amet urna. In ornare posuere risus et vestibulum."
        val list = mutableListOf<Book>()
        _books.value = list
    }

    private fun loadItem() {
        _itemToDisplay = savedStateHandle.getLiveData<Book>(ITEM)
        FirebaseFirestore.getInstance().collection("books").document(_itemToDisplay.value?.id!!).addSnapshotListener(object :
            EventListener<DocumentSnapshot> {
            override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.w(Constants.VMTAG, "Listen failed.", error)
                    return
                }
                if(value?.data?.get("Name") != null) {
                    var book: Book = Book(
                        _itemToDisplay.value?.id!!,
                        value?.data?.get("Image").toString(),
                        value?.data?.get("Name").toString(),
                        value?.data?.get("Kind").toString(),
                        value?.data?.get("Description").toString(),
                    )
                    itemToDisplay.value = book
                }
            }
        })

    }
    fun addToCart() {
        viewModelScope.launch {
            cartRepository.addCart(
                _itemToDisplay.value!!,
                CurrentUserInfo.getInstance().currentProfile
            )
        }
    }
}