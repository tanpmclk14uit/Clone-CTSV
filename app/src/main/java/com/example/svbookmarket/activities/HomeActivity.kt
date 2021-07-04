package com.example.svbookmarket.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.marginTop
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.adapter.*
import com.example.svbookmarket.activities.animation.TranslateAnimationUtil
import com.example.svbookmarket.activities.common.AppUtil
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.common.Constants.ACTIVITY
import com.example.svbookmarket.activities.common.Constants.ACTIVITY.*
import com.example.svbookmarket.activities.common.Constants.ITEM
import com.example.svbookmarket.activities.common.MarginItemDecoration
import com.example.svbookmarket.activities.common.RecyclerViewItemMargin
import com.example.svbookmarket.activities.data.DataSource
import com.example.svbookmarket.activities.data.Response.*
import com.example.svbookmarket.activities.model.AppAccount
import com.example.svbookmarket.activities.model.Book
import com.example.svbookmarket.activities.model.Cart
import com.example.svbookmarket.activities.viewmodel.HomeViewModel
import com.example.svbookmarket.databinding.ActivityHomeBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import io.grpc.InternalChannelz.id
import java.lang.Thread.sleep


@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), FeaturedAdapter.OnBookClickLitener,
    CategoryAdapter.onCategoryItemClick, SuggestAdapter.OnSuggestClickListener {
    lateinit var binding: ActivityHomeBinding

    val viewModel: HomeViewModel by viewModels()

    private var adsAdapter = AdvertiseAdapter(mutableListOf())
    private var categoryAdapter = CategoryAdapter(mutableListOf(), this@HomeActivity)
    private var suggestAdapter = SuggestAdapter(mutableListOf(), this)
    private var moreAdapter = FeaturedAdapter(mutableListOf(), this)

    var isBackPressedOnce = false


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // watch data change
        watchForDataChange()

        setCategoryAdapter()
        setSuggestAdapter() // feature
        setMoreAdapter()
        setupNavigation()
        setUpBottomNavigationView()

       binding.nestedScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
           if(scrollY - oldScrollY >0){
               binding.bottomNavigation.visibility = View.GONE

           }else{
               binding.bottomNavigation.visibility = View.VISIBLE
           }
       }


    }

    private fun watchForDataChange() {
        viewModel.getBookFrom().observe(this, { changes ->
            // filter theo so luot mua
            val b = changes.toList().sortedByDescending { it.rate }
            val top15 = b.take(15)


            suggestAdapter.addBooks(top15)
            moreAdapter.addBooks(b)
        })

    }

    private fun setSuggestAdapter() {
        binding.rcSuggest.apply {
            adapter = suggestAdapter
            layoutManager = LinearLayoutManager(
                context,
                RecyclerView.HORIZONTAL,
                false
            )
            addItemDecoration(MarginItemDecoration(spaceSize = 24, isHorizontalLayout = true))
            setHasFixedSize(true)
            LinearSnapHelper().attachToRecyclerView(this)

        }
    }


    private fun setCategoryAdapter() {
        viewModel.category.observe(this, Observer { newCategory ->
            categoryAdapter.addCategory(newCategory)
            binding.bookCategory.apply {
                adapter = categoryAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(MarginItemDecoration(spaceSize = 20, isHorizontalLayout = true))
            }
        })
    }

    private fun setMoreAdapter() {
        binding.hRcMore.apply {
            adapter = moreAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            addItemDecoration(MarginItemDecoration(spaceSize = 15, spanCount = 2))
        }
    }

    /**
     * navigate when click on screen
     */

    private fun setupNavigation() {
//        findViewById<ImageView>(R.id.tb_menu).setOnClickListener { startIntent(MENU) }
        findViewById<TextInputEditText>(R.id.edtSearchText).setOnClickListener { startIntent(SEARCH) }
//       findViewById<ImageView>(R.id.tb_cart).setOnClickListener { startIntent("cart") }
        findViewById<TextView>(R.id.h_allCategory).setOnClickListener { startIntent(CATEGORY) }
        findViewById<TextView>(R.id.h_allFeature).setOnClickListener { startIntent(FEATURE) }

    }


    /**
     * start an intent for navigating
     */
    private fun startIntent(name: ACTIVITY) {
        val intent = when (name) {
            PROFILE -> {
                Intent(this, UserManageActivity::class.java)

            }
            MENU -> Intent(this, MenuActivity::class.java)
            SEARCH -> Intent(this, SearchActivity::class.java)
            CART -> Intent(this, CartActivity::class.java)
            CATEGORY -> {
                Intent(this, CategoryActivity::class.java)
            }
            FEATURE -> Intent(this, FeatureActivity::class.java)
            CATEGORY_DETAIL -> Intent(this, CategoryDetailActivity::class.java)
                .putExtra(CategoryDetailActivity.CATEGORY_TYPE, name)
        }
        startActivity(intent)
    }

    /**
     * put single book into intent
     */
    private fun putBookIntoIntent(item:Book):Intent{
        val bundle = Bundle()
        bundle.putParcelable(ITEM, item)
        val i = Intent(this, ItemDetailActivity::class.java)
        i.putExtras(bundle)
        return i
    }

    /**
     * put array of category into intent
     */
    private fun putBooksOfCategoryIntoIntent(categoryName: String): Intent {
        val bundle = Bundle()
        val i = viewModel.getBooksOfCategory(categoryName)

        //put data into bundle
        bundle.putParcelableArrayList(CATEGORY_DETAIL.toString(), i)

        // put bundle into intent
        return Intent(this, CategoryDetailActivity::class.java)
            .putExtras(bundle)
            .putExtra(CategoryDetailActivity.CATEGORY_TYPE, categoryName)
    }

    private fun navigate(mIntent: Intent) = this.binding.root.context.startActivity(mIntent)

    override fun onSuggestClick(book: Book) {
        val i = putBookIntoIntent(book)
        navigate(i)
    }

    /**
     * put book to intent then start navigation
     */
    override fun onBookClick(book: Book) {
        val intent =
            Intent(binding.root.context, ItemDetailActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(ITEM, book)
        intent.putExtras(bundle)
        binding.root.context.startActivity(intent)
    }


    /**
     * put category name to intent then start navigation
     */
    override fun onCategoryItemClick(categoryName: String) {
        val i = putBooksOfCategoryIntoIntent(categoryName)
        navigate(i)
    }


    /**
     * prevent instant exit
     */
    override fun onBackPressed() {
        if (isBackPressedOnce) {
            super.onBackPressed()
            return
        }

        this.isBackPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { isBackPressedOnce = false }, 2000)
    }
    private fun setCountOfBottomMeow() {
        viewModel.getCart(AppUtil.currentAccount).observe(this, Observer { change ->
            binding.bottomNavigation.setCount(id = 3, change.size.toString())
        })
    }
    private fun setUpBottomNavigationView(){

        binding.bottomNavigation.add(MeowBottomNavigation.Model(id= 1, R.drawable.ic_baseline_person_24))
        binding.bottomNavigation.add(MeowBottomNavigation.Model(id= 2, R.drawable.ic_home))
        binding.bottomNavigation.add(MeowBottomNavigation.Model(id= 3, R.drawable.ic_baseline_shopping_cart_24))
       setCountOfBottomMeow()
        binding.bottomNavigation.show(id = 2,   true)
        binding.bottomNavigation.setOnClickMenuListener {
            if(it.id ==3){
                startIntent(Constants.ACTIVITY.CART)
            }
            if(it.id ==1){
                startIntent(Constants.ACTIVITY.PROFILE)

            }

        }
        binding.bottomNavigation.setOnShowListener {
            if(it.id ==3){
               binding.bottomNavigation.show(2, true)
            }
            if(it.id ==1){
                binding.bottomNavigation.show(2, true)
            }
        }
    }


}
