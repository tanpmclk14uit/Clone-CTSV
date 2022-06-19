package com.example.svbookmarket.activities.model

import com.example.svbookmarket.activities.common.Constants

class OrderBankLoansIdentify(
    id: String = "",
    studentEmail: String,
    status: String,
    dateTime: String,
    var tuitionKind: String,
    var familyKind: String
) : Order(id, studentEmail, status, dateTime) {

    init {
        this.kind = Constants.OrderKind.GXNVVNH.toString()
    }

    override fun toString(): String {
        return "$kind, $id,$studentEmail,$status,$dateTime,$tuitionKind,$familyKind"
    }
    override fun isValidOrder(): Boolean {
        return studentEmail.isNotBlank() &&
                status.isNotBlank() &&
                dateTime.isNotBlank() &&
                tuitionKind.isNotBlank() &&
                familyKind.isNotBlank()
    }
}