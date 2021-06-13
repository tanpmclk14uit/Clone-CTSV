package com.example.svbookmarket.activities.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.svbookmarket.activities.model.Book

class ItemDetailViewModelFactory(private val book: Book?):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemDetailViewModel::class.java))
            return ItemDetailViewModel(book) as T
        throw IllegalArgumentException("Wrong view model class")
    }
}
