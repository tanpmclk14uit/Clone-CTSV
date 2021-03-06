package com.example.svbookmarket.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.adapter.CommentAdapter
import com.example.svbookmarket.activities.common.AppUtil
import com.example.svbookmarket.activities.common.Constants.DEFAULT_IMG_PLACEHOLDER
import com.example.svbookmarket.activities.model.Book
import com.example.svbookmarket.activities.viewmodel.ItemDetailViewModel
import com.example.svbookmarket.databinding.ActivityItemDetailBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityItemDetailBinding
    private val viewModel: ItemDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.itemToDisplay.observe(this, changeObserver)

        //getItemToDisplayFromBundle()
        setupOnlickListener()
        onDeleteListen()

        binding.expandDes.setOnClickListener {
            onExpandClick()
        }
    }
    private var currentBookId: String? = null
    private val changeObserver = Observer<Book> { value ->
        value?.let {
            if (value.Name != "null") {
                currentBookId = it.id
                //getAllComment()
                binding.idTitle.text = it.Name
                binding.idDescription.text = it.Description
                it.Image?.let { uri -> loadImageFromUri(Uri.parse(uri)) }

            } else {
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }
    }

    private fun onExpandClick(){
        if(binding.idDescription.visibility == View.GONE){
            binding.idDescription.visibility = View.VISIBLE
            binding.expandDes.setBackgroundResource(R.drawable.ic_baseline_expand_less_24)

        }else{
            binding.idDescription.visibility = View.GONE
            binding.expandDes.setBackgroundResource(R.drawable.ic_baseline_expand_more_24)
        }
    }
    private fun addItemToCart() {
        viewModel.addToCart()
    }

    private fun setupOnlickListener() {
        binding.idAddCart.setOnClickListener { addItemToCart() }
        binding.idBack.setOnClickListener { onBackPressed() }
    }

    private fun loadImageFromUri(uri: Uri) {
        Glide
            .with(baseContext)
            .load(uri)
            .fitCenter()
            .placeholder(DEFAULT_IMG_PLACEHOLDER)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.idThumbnail)
    }

    private fun onDeleteListen() {
        FirebaseFirestore.getInstance().collection("books").addSnapshotListener { snapshots, e ->
            e?.let {
            }
            for (dc in snapshots!!.documentChanges) {
                if (dc.document.id == viewModel.itemToDisplay.value?.id)
                    when (dc.type) {
                        DocumentChange.Type.REMOVED -> {
                            if (currentFocus == ItemDetailActivity::class.java) {
                                startActivity(Intent(this, HomeActivity::class.java))
                            }
                            finish()
                        }
                    }
            }
        }
    }
}