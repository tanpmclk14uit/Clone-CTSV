package com.example.svbookmarket.activities.model

abstract class Order(
    var id: String ="",
    var studentEmail: String = "",
    var status: String ="",
    var dateTime: String ="",
    var kind: String ="",
    var cancelReason: String = "",
){
    abstract fun isValidOrder(): Boolean
}
