package com.example.svbookmarket.activities.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.ItemDetailActivity
import com.example.svbookmarket.activities.common.Constants.ITEM
import com.example.svbookmarket.activities.data.FullBookList
import com.example.svbookmarket.activities.model.Book

class RecentSearchAdapter(context: Context, private val lstRecentSearch: MutableList<Book>): RecyclerView.Adapter<RecentSearchAdapter.RecentSearchViewHolder> (){
     class RecentSearchViewHolder(RecentSearchCard: View): RecyclerView.ViewHolder(RecentSearchCard)
    {
        val recentSearchName: TextView = RecentSearchCard.findViewById(R.id.tvRecentSearchName)
        val recentSearchTime: TextView = RecentSearchCard.findViewById(R.id.tvRecentSearchTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_recent_search, parent, false)
        return RecentSearchAdapter.RecentSearchViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecentSearchViewHolder, position: Int) {
               holder.recentSearchName.text = lstRecentSearch[position].Name
                holder.recentSearchTime.text = lstRecentSearch[position].Kind
        holder.itemView.setOnClickListener {
            var intentDetail = Intent(holder.itemView.context, ItemDetailActivity::class.java)
            var bundle = Bundle()
            for(book in FullBookList.getInstance().lstFullBook)
            {
                if (book.Name == holder.recentSearchName.text)
                {
                    bundle.putParcelable(ITEM, book)
                }
            }
            intentDetail.putExtra("Bundle",bundle)
            ContextCompat.startActivity(holder.itemView.context, intentDetail, bundle);
        }
    }

    override fun getItemCount() = lstRecentSearch.size

}