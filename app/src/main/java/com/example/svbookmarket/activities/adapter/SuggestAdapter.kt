package com.example.svbookmarket.activities.adapter

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.luminance
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
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
                it.author.text = Author
                it.des.text = Description

                Glide
                    .with(holder.itemView)
                    .asBitmap()
                    .load(Image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .placeholder(DEFAULT_IMG_PLACEHOLDER)
                    .transition(BitmapTransitionOptions.withCrossFade())
                    .into(object : CustomViewTarget<ImageView, Bitmap>(it.thumbnail) {
                        override fun onLoadFailed(errorDrawable: Drawable?) {}

                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {

                            if (resource != null) {
                                //init viewholder palette
                                it.getPaletteFromBitmap(resource)
                                //set items color
                                it.setColor()

                                val didSucceedTransition = transition!!.transition(
                                    resource,
                                    BitmapImageViewTarget(it.thumbnail)
                                )
                                if (!didSucceedTransition) it.thumbnail.setImageBitmap(resource)
                            }
                        }

                        override fun onResourceCleared(placeholder: Drawable?) {
                            it.thumbnail.setImageDrawable(placeholder)
                        }

                    })




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


    @RequiresApi(Build.VERSION_CODES.O)
    fun setColor() {
        setMTextColor()
        setBackgroundColor()
    }

    fun getPaletteFromBitmap(bitmap: Bitmap) {
        this.palette = Palette.from(bitmap).generate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMTextColor() {
        val color = this.computeTextColor()
        title.setTextColor(color)
        author.setTextColor(color)
        des.setTextColor(color)
    }

    /**
     * return text color based on background color
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun computeTextColor(): Int {
        setDominantColor()
        // use dominant color to compute color
        return if (this.dominantColor.luminance > 0.5) Color.BLACK else Color.WHITE
    }

    private fun setDominantColor() {
        this.dominantColor = palette?.getDominantColor(DEFAULT_COLOR)!!
    }

    private fun setBackgroundColor() {
        // get muted color from image, if cannot, return default color (green)
        var c = palette?.lightMutedSwatch?.rgb ?: DEFAULT_COLOR

        // if cannot get light muted color, use dominant color
        if (c.compareTo(DEFAULT_COLOR) == 0) {
            Log.i("comparecolor", "true")
            c = palette?.dominantSwatch?.rgb ?: DEFAULT_COLOR
        }
        (this.itemView as MaterialCardView).setCardBackgroundColor(c)
    }
}

interface OnSuggestClickListener {
    fun onSuggestClick(book: Book)
}
}