package com.example.svbookmarket.activities.model

import com.example.svbookmarket.activities.common.Constants

class OrderStudentIdentify(
    id: String = "",
    studentEmail: String,
    status: String,
    dateTime: String,
    var reason: String
) : Order(id, studentEmail, status, dateTime) {

    init {
        this.kind = Constants.OrderKind.GXNSV.toString()
    }
    override fun toString(): String {
        return "$kind,$id,$studentEmail,$status,$dateTime,$reason"
    }
    override fun isValidOrder(): Boolean {
        return studentEmail.isNotBlank() &&
                status.isNotBlank() &&
                dateTime.isNotBlank() &&
                reason.isNotBlank()
    }
}
