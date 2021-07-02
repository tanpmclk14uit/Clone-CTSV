package com.example.svbookmarket.activities.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.common.Constants.ITEM
import com.example.svbookmarket.activities.data.BookRepository
import com.example.svbookmarket.activities.data.CartRepository
import com.example.svbookmarket.activities.model.Book
import com.google.common.base.Objects
import com.google.firebase.firestore.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ItemDetailViewModel @Inject constructor (private val savedStateHandle: SavedStateHandle,
                                               private val cartRepository: CartRepository,
                                               private  val bookRepository: BookRepository) : ViewModel() {
    private var _books = MutableLiveData<MutableList<Book>>()
    val books get() = _books

    private var _itemToDisplay = MutableLiveData<Book>()
    val itemToDisplay get() = _itemToDisplay

    fun addItemToCart() = itemToDisplay.value?.let { _books.value?.add(it) }

    init {
        loadBooks()
        loadItem()
    }

    private fun loadBooks() {
        val des =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut egestas in ligula a maximus. Mauris venenatis, neque vitae sollicitudin dapibus, dolor lorem tempor orci, eget viverra ante eros non sem. Nam maximus dignissim purus, ac cursus felis. Duis maximus odio nunc, nec dapibus ligula ultricies non. Aliquam efficitur sapien ut nisl aliquet, eget malesuada urna egestas. Nullam sed orci urna. Praesent iaculis dapibus urna, at rutrum ipsum aliquet at. Pellentesque pellentesque augue vel tortor convallis aliquam. Etiam porttitor id urna at dictum. Donec scelerisque auctor quam, id varius ligula. Aliquam eget nibh et urna dapibus vestibulum. Donec mauris ipsum, aliquet ut risus ac, efficitur porta tortor. Donec ac libero ut leo lobortis elementum. Nunc commodo metus nunc.\nPhasellus iaculis nisi a leo vehicula sodales. Fusce hendrerit quam eget tortor semper ultrices. Vivamus rhoncus molestie massa et volutpat. Proin cursus ex ac diam ornare consectetur. Curabitur vitae congue lectus. Pellentesque a purus fermentum, varius est vel, faucibus risus. Nullam vitae massa vitae diam fermentum condimentum vitae a sem. Etiam eu lorem a libero sollicitudin placerat.\nDuis sodales imperdiet quam, vitae laoreet ex fermentum non. Vivamus convallis magna in justo laoreet, a ornare enim sollicitudin. Nam laoreet neque eu tempus commodo. In sollicitudin enim sit amet rutrum posuere. Integer mattis aliquet posuere. Integer blandit mauris vel erat molestie faucibus. Ut non urna in urna interdum venenatis non sit amet urna. In ornare posuere risus et vestibulum."
        val list = mutableListOf<Book>()

//        for (i in 1..12) {
//            list.add(
//                Book(
//                    Uri.parse(
//                        "data:image/jpeg;base64,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"
//                    ),
//                    "Sample2", "Sample2", 120000, 2.0, "none", 50, des
//                ),
//            )
//            list.add(
//                Book(
//                    Uri.parse("https://kienviet.net/wp-content/uploads/2019/08/12-diem-du-lich-phai-den-tai-paris-danh-cho-kts-phan-1-kien-viet.jpg"),
//                    "Sample", "Sample", 100000, 3.5, "none", 50, des
//                )
//            )
//        }
        _books.value = list

    }

    private fun loadItem() {
//        _itemToDisplay.value = Book().apply {
//            title = args.get(ItemDetailActivity.ITEM).toString()
//            author = args.get(ItemDetailActivity.AUTHOR).toString()
//            price = args.get(ItemDetailActivity.PRICE).toString().toLong()
//            rating = args.get(ItemDetailActivity.RATEPOINT).toString().toDouble()
//            imageURL = args.get(ItemDetailActivity.THUMBNAIL_URL).toString().toUri()
//            description = args.get(ItemDetailActivity.DESCRIPTION).toString()
//
//        }
        //item?.let { _itemToDisplay.value = it}
        // itemToDisplay?.let {
        //    bookRepository.getBooksFromCloudFirestore1().
        //}
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
                        value?.data?.get("Author").toString(),
                        value?.data?.get("Price").toString().toDouble().roundToInt(),
                        value?.data?.get("rate").toString().toDouble().roundToInt(),
                        value?.data?.get("Kind").toString(),
                        value?.data?.get("Counts").toString().toDouble().roundToInt(),
                        value?.data?.get("Description").toString(),
                        value?.data?.get("Saler").toString(),
                        value?.data?.get("SalerName").toString(),
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