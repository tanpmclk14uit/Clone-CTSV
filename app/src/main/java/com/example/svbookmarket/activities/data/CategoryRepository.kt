package com.example.svbookmarket.activities.data

import androidx.annotation.DrawableRes
import androidx.lifecycle.MutableLiveData
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.common.Constants.CATEGORY.*
import com.example.svbookmarket.activities.model.Category
import javax.inject.Inject

class CategoryRepository @Inject constructor( /*database */) {
    /**
     * get data from database then save it into _category
     * now use temp data for test
     */
    private var _category = MutableLiveData<MutableList<Category>>()
    val category get() = _category

    @DrawableRes
    fun getCollectionImgSource(categoryName: String): Int {
        return R.drawable.bg_increasebtn
    }


    private fun loadData() {
        _category.value = mutableListOf(
            Category(R.drawable.bg_increasebtn, CHUNG),
            Category(R.drawable.bg_increasebtn,  HV),
            Category(R.drawable.bg_increasebtn, VB2),
            Category(R.drawable.bg_increasebtn, LTCQ),
            Category(R.drawable.bg_increasebtn, NB),
            Category(R.drawable.bg_increasebtn, PH),
        )
    }

    init {
        loadData()
    }

}