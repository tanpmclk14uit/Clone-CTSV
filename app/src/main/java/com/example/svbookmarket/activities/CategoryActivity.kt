package com.example.svbookmarket.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.common.Constants.ACTIVITY.CATEGORY_DETAIL
import com.example.svbookmarket.activities.common.Constants.CATEGORY.*
import com.example.svbookmarket.activities.model.Book
import com.example.svbookmarket.activities.viewmodel.CategoryViewModel
import com.example.svbookmarket.databinding.ActivityCategoryBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CategoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityCategoryBinding

    private val viewModel: CategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getBookFrom()

        setupView()
        setupNavigation()
    }

    private fun setupNavigation() {
        binding.apply {
            cBackButton.setOnClickListener { onBackPressed() }
            cArt.setOnClickListener { startIntent(CHUNG.toString()) }
            cComic.setOnClickListener { startIntent(VB2.toString()) }
            cFiction.setOnClickListener { startIntent(PH.toString()) }
            cNovel.setOnClickListener { startIntent(HV.toString()) }
            cBusiness.setOnClickListener { startIntent(LTCQ.toString()) }
            cTechnology.setOnClickListener { startIntent(NB.toString()) }
        }
    }

    private fun setupView() {
        val art = binding.cArtI
        val comic = binding.cComicI
        val fiction = binding.cFictionI
        val novel = binding.cNovelI
        val bus = binding.cBusinessI
        val tech = binding.cTechnologyI

        val list = listOf(
            Pair(R.drawable.bg_gradient_splash, art),
            Pair(R.drawable.bg_gradient_splash, comic),
            Pair(R.drawable.bg_gradient_splash, fiction),
            Pair(R.drawable.bg_gradient_splash, bus),
            Pair(R.drawable.bg_gradient_splash, tech),
            Pair(R.drawable.bg_gradient_splash, novel),
        )
        loadImage(list)

    }

    private fun loadImage(list: List<Pair<Int, ImageView>>) {
        for (pair in list) {
            Glide.with(applicationContext)
                .load(pair.first)
                .centerCrop()
                .override(500, 800)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(pair.second)
        }
    }

    private fun startIntent(type: String) {
        if (type != "back" && type != "search") {
            if (viewModel.isBookLoaded.value!!) {
                val filteredColl = viewModel.getBooksOfCategory(type)
                val intent = Intent(this, CategoryDetailActivity::class.java)
                putDataToIntent(intent, filteredColl, type)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Đang tải dữ liệu vui lòng đợi trong ít phút", Toast.LENGTH_LONG).show()
            }
        } else {
            Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * put into and return intent
     */
    private fun putDataToIntent(i: Intent, data: ArrayList<Book>?, type: String): Intent {
        val bundle = putDataToBundle(data)
        i.putExtras(bundle)
            .putExtra(CategoryDetailActivity.CATEGORY_TYPE, type)
        return i
    }

    /**
     * put into and return bunde
     */
    private fun putDataToBundle(data: ArrayList<Book>?): Bundle {
        val bundle = Bundle()
        bundle.putParcelableArrayList(CATEGORY_DETAIL.toString(), data)
        return bundle
    }

}