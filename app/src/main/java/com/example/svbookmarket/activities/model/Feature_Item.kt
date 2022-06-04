package com.example.svbookmarket.activities.model

data class Feature_Item (var imgBookCover: Int,
                         var textAuthor: String,
                         var textBookName: String,
                         var textPrice: String = "",
                         var NumReviews: String = "0 Reviews",
                         var NumStart: Double = 0.0)