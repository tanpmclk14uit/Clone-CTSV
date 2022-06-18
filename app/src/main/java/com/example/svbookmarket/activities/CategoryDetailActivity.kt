package com.example.svbookmarket.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.adapter.SuggestAdapter
import com.example.svbookmarket.activities.common.Constants.ACTIVITY.CATEGORY_DETAIL
import com.example.svbookmarket.activities.common.Constants.ITEM
import com.example.svbookmarket.activities.common.MarginItemDecoration
import com.example.svbookmarket.activities.model.Book
import com.example.svbookmarket.activities.viewmodel.CategoryDetailViewModel
import com.example.svbookmarket.databinding.ActivityCategoryDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class CategoryDetailActivity : AppCompatActivity(), SuggestAdapter.OnSuggestClickListener {
    companion object {
        const val CATEGORY_TYPE = "CATEGORY"
    }

    lateinit var binding: ActivityCategoryDetailBinding
    lateinit var items: ArrayList<Book>
    private val viewmodel: CategoryDetailViewModel by viewModels()

    private val detailApt = SuggestAdapter(mutableListOf(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
    }

    private fun setupView() {
        setDetailAdapter()
        displayCategory(getCategoryNameFromIntent())
    }


    @SuppressLint("SetTextI18n")
    private fun displayCategory(categoryName: String) {

        binding.cdClName.text = "Thông báo ${categoryName.toLowerCase(Locale.ROOT)}"
        binding.cdTitle.text = categoryName
        val backgroundResId = getCategorybg(categoryName)
        // load image
        Glide
            .with(baseContext)
            .load(backgroundResId)
            .centerCrop()
            .placeholder(R.drawable.bg_button_white)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.cdCover);


        binding.cdRc.apply {
            adapter = detailApt
            addItemDecoration(MarginItemDecoration(spaceSize = 14, spanCount = 2))
        }

    }

    @DrawableRes
    private fun getCategorybg(categoryName: String): Int =
        viewmodel.getCollectionImgSource(categoryName)

    private fun setDetailAdapter() {
        val books = getBooksFromIntent()
        detailApt.addBooks(books)
    }

    private fun getBooksFromIntent(): ArrayList<Book> {
        val bundle = intent.extras
        items = bundle?.getParcelableArrayList<Book>(CATEGORY_DETAIL.toString()) as ArrayList<Book>
        return items
    }

    private fun getCategoryNameFromIntent(): String = intent.getStringExtra(CATEGORY_TYPE)!!


    override fun onSuggestClick(book: Book) {
        val i = putBookIntoIntent(book)
        navigate(i)
    }
    private fun navigate(mIntent: Intent) = this.binding.root.context.startActivity(mIntent)

    private fun putBookIntoIntent(item: Book): Intent {
        val bundle = Bundle()
        bundle.putParcelable(ITEM, item)
        val i = Intent(this, ItemDetailActivity::class.java)
        i.putExtras(bundle)
        return i
    }
}






