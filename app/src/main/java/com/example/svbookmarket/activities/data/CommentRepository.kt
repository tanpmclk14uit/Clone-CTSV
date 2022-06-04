package com.example.svbookmarket.activities.data

import com.example.svbookmarket.activities.common.Constants
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import java.util.*
import javax.inject.Inject
import javax.inject.Named


class CommentRepository@Inject constructor(
    @Named(Constants.BOOK_REF) private val bookCollRef: CollectionReference
) {
    fun getAllCommentOfBook(bookId: String): Query{
        return bookCollRef.document(bookId).collection("Comment").orderBy(
            "time",
            Query.Direction.DESCENDING
        )
    }
    fun createNewComment(bookId: String, comment: String, userId: String){
        val time = Date()
        val commentMap = hashMapOf(
            "time" to time,
            "userId" to userId,
            "comment" to comment,
        )
        bookCollRef.document(bookId).collection("Comment").add(commentMap)

    }
}