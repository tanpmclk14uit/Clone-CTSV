package com.example.svbookmarket.activities.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.svbookmarket.activities.model.Cart
import com.example.svbookmarket.activities.model.Comment
import com.example.svbookmarket.databinding.CommentCardBinding
import com.example.svbookmarket.databinding.ItemBillingBinding

class CommentAdapter(
    private var commentList: MutableList<Comment>
): RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    class ViewHolder(binding: CommentCardBinding): RecyclerView.ViewHolder(binding.root){
        val userName = binding.userName
        val userComment = binding.userComment
    }
    fun onChange(newList: MutableList<Comment>) {
        commentList = newList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return CommentAdapter.ViewHolder(
            CommentCardBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recentComment: Comment = commentList[position]
        holder.apply {
            userName.text = recentComment.userName
            userComment.text = recentComment.userComment
        }
    }

    override fun getItemCount(): Int {
        return commentList.size
    }
}