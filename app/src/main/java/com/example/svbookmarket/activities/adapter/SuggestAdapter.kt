package com.example.svbookmarket.activities.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.common.Constants.DEFAULT_IMG_PLACEHOLDER
import com.example.svbookmarket.activities.model.Book
import com.google.android.material.card.MaterialCardView


class SuggestAdapter(
    private val books: MutableList<Book>,
    private val listener: OnSuggestClickListener
) :
    RecyclerView.Adapter<SuggestAdapter.SuggestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.card_suggest, parent, false)
        return SuggestViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: SuggestViewHolder, position: Int) {
        with(books[position]) {
            holder.let {
                it.title.text = Name
                it.author.text = SalerName
                it.des.text = Description

                Glide
                    .with(holder.itemView)
                    .asBitmap()
                    .load(Image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .placeholder(DEFAULT_IMG_PLACEHOLDER)
                    .transition(BitmapTransitionOptions.withCrossFade())
                    .into(it.thumbnail)

            if (it.itemView is MaterialCardView) {
                it.itemView.setOnClickListener {
                    listener.onSuggestClick(this)
                }
            }
        }
    }
}


fun addBooks(book: List<Book>) {
    if (this.books.isNotEmpty()) {
        this.books.clear()
    }
    this.books.addAll(book)
    notifyDataSetChanged()
}

override fun getItemCount() = books.size

inner class SuggestViewHolder(ViewHolder: View) : RecyclerView.ViewHolder(ViewHolder) {
    val thumbnail: ImageView = ViewHolder.findViewById(R.id.imgSuggest)
    val title: TextView = ViewHolder.findViewById(R.id.tvBookSuggest)
    val author: TextView = ViewHolder.findViewById(R.id.tvAuthorSuggest)
    val des: TextView = ViewHolder.findViewById(R.id.sg_description)

    val DEFAULT_COLOR = ContextCompat.getColor(
        this.itemView.context,
        R.color.white
    )

    var palette: Palette? = null
    private var dominantColor = DEFAULT_COLOR


}

interface OnSuggestClickListener {
    fun onSuggestClick(book: Book)
}
}