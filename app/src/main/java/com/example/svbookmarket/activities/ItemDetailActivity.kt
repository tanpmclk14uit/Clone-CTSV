package com.example.svbookmarket.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
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

    // TODO: 11/06/2021 thay bttoin add to cart to material button</todo>
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

    }

    private val changeObserver = Observer<Book> { value ->
        value?.let {
            if (value.Name != "null") {
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