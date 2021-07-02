package com.example.svbookmarket.activities.model

data class Order(
    var id: String ="",
    var userDeliverAddress: UserDeliverAddress = UserDeliverAddress(),
    var status: String ="",
    var totalPrince: String = "0",
    var dateTime: String ="",
    var listbooks: ArrayList<Cart> = ArrayList(),
)
