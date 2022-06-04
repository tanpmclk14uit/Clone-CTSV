package com.example.svbookmarket.activities.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.model.Category

class CategoryAdapter(private val items: MutableList<Category>, private val listener: onCategoryItemClick) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val startTime = System.currentTimeMillis()
        with(items[position]){
            holder.let {
                it.name.text = name
                Glide
                    .with(holder.itemView)
                    .load(backgroundResId)
                    .centerCrop()
                    .placeholder(Constants.DEFAULT_IMG_PLACEHOLDER)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(it.background)
                //setup listener
                it.itemView.setOnClickListener { listener.onCategoryItemClick(this.name) }
            }
        }
        Log.i("TAG", "bindView time: " + (System.currentTimeMillis() - startTime));
    }
    override fun getItemCount(): Int = items.size

    fun addCategory(category: List<Category>) {
        if (this.items.isNotEmpty()) {
            this.items.clear()
        }
        this.items.addAll(category)
        notifyDataSetChanged()
    }



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.categoryName)
        val background: ImageView = view.findViewById(R.id.categoryImage)
    }

    interface onCategoryItemClick {
        fun onCategoryItemClick(categoryName:String)
    }

}