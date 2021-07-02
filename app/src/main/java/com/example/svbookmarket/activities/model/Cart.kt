package com.example.svbookmarket.activities.model

data class Cart(
    var id: String ="",
    var imgUrl: String ="",
    var name: String="",
    var author: String="",
    var numbers: Int=0,
    var price: Long=0,
    var saler: String="",
    var salerName: String="",
    var isChose: Boolean=false,
)