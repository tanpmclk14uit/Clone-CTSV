package com.example.svbookmarket.activities.viewmodel

import androidx.lifecycle.ViewModel
import com.example.svbookmarket.activities.common.AppUtil
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.data.OrderRepository
import com.example.svbookmarket.activities.model.Order
import com.example.svbookmarket.activities.model.OrderBankLoansIdentify
import com.example.svbookmarket.activities.model.OrderStudentIdentify
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RegisterNewOnlineServiceOrderViewModel @Inject constructor(private val orderRepository: OrderRepository) :
    ViewModel() {

    var kind: Constants.OrderKind = Constants.OrderKind.GXNSV
    var reason: String = ""
    var tuitionKind: String = ""
    var familyKind: String = ""

    private fun getCurrentDate():String{
        val sdf = SimpleDateFormat("HH:mm:ss dd-MM-yyyy ")
        return sdf.format(Date())
    }

    fun makeOrder():Order{
        return if(kind == Constants.OrderKind.GXNSV){
            OrderStudentIdentify(
                studentEmail = AppUtil.currentAccount.email,
                status = Constants.OrderStatus.WAITING.toString(),
                dateTime = getCurrentDate(),
                reason = reason
            )
        }else{
            OrderBankLoansIdentify(
                studentEmail = AppUtil.currentAccount.email,
                status = Constants.OrderStatus.WAITING.toString(),
                dateTime = getCurrentDate(),
                tuitionKind = tuitionKind,
                familyKind = familyKind
            )
        }
    }

}