package com.example.svbookmarket.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
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
import java.text.DecimalFormat

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
        binding.idSalerName.setOnClickListener {
            startActivity(Intent(baseContext, ProfileActivity::class.java))
        }

        binding.expandDes.setOnClickListener {
            onExpandClick()
        }
        setUpComment()
        binding.postComment.setOnClickListener {
            postComment()
        }
    }
    private val commentAdapter: CommentAdapter = CommentAdapter(mutableListOf())
    private fun setUpComment(){

        binding.recycleViewComment.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
        }
    }
    private fun getAllComment(){
        if(currentBookId != null){
            viewModel.loadComment(currentBookId!!).observe(this, { changes ->
                commentAdapter.onChange(changes)
                binding.commentCount.text = "(${changes.size.toString()})"
            })
        }
    }
    private var currentBookId: String? = null
    private val changeObserver = Observer<Book> { value ->
        value?.let {
            if (value.Name != "null") {
                currentBookId = it.id
                getAllComment()
                binding.idTitle.text = it.Name
                val formatter = DecimalFormat("#,###")
                binding.idPrice.text = formatter.format(it.Price.toLong()) + " đ"
                binding.idAuthor.text = it.Author
                binding.idRate.text = it.rate.toString()
                binding.idDescription.text = it.Description
                binding.idSalerName.text = "Sale by " + it.SalerName
                binding.idCount.text = it.Counts.toString()
                AppUtil.currentSeller.email = it.Saler.toString()
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
    private fun postComment(){
        if(binding.comment.text.isNotBlank() && currentBookId != null){
            viewModel.postComment(currentBookId!!, AppUtil.currentAccount.email,binding.comment.text.toString())
            binding.comment.text.clear()
        }
    }



    private fun startIntent(type: String) {
        val intent = Intent(this, CartActivity::class.java)
        startActivity(intent)
    }

    private fun addItemToCart() {
        viewModel.addToCart()
    }

    fun setupOnlickListener() {
        binding.idBuy.setOnClickListener {
            addItemToCart()
            startIntent("buy")
        }
        binding.idAddCart.setOnClickListener { addItemToCart() }
        binding.idBack.setOnClickListener { onBackPressed() }
    }

    private fun loadImageFromUri(uri: Uri) {
        Glide
            .with(baseContext)
            .load(uri)
            .centerCrop()
            .placeholder(DEFAULT_IMG_PLACEHOLDER)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.idTnBackground)
        Glide
            .with(baseContext)
            .load(uri)
            .centerCrop()
            .placeholder(DEFAULT_IMG_PLACEHOLDER)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.idThumbnail)
    }

    fun onDeleteListen() {
        FirebaseFirestore.getInstance().collection("books").addSnapshotListener { snapshots, e ->
            e?.let {
                Log.d("0000000", e.message!!)
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