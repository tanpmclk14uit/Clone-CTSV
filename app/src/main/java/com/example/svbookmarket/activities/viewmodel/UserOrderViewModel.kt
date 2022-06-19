package com.example.svbookmarket.activities.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.data.OrderRepository
import com.example.svbookmarket.activities.model.Book
import com.example.svbookmarket.activities.model.Order
import com.example.svbookmarket.activities.model.OrderBankLoansIdentify
import com.example.svbookmarket.activities.model.OrderStudentIdentify
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

@HiltViewModel
class UserOrderViewModel @Inject constructor(private val orderRepository: OrderRepository) :
    ViewModel() {
    private var _order = MutableLiveData<MutableList<Order>>()
    val orders get() = _order
    var reason: String = ""



    init {
        _order = getAllOrder()
    }


    private fun getAllOrder(): MutableLiveData<MutableList<Order>> {
        orderRepository.getAllOrderFromCloudFireStore().addSnapshotListener { value, error ->
            if (error != null) {
                Log.w(Constants.VMTAG, "Listen failed.", error)
            } else {
                val orderList: MutableList<Order> = ArrayList()
                for (doc in value!!) {
                    if (doc["kind"].toString() == Constants.OrderKind.GXNSV.toString()) {
                        val order = OrderStudentIdentify(
                            doc.id,
                            doc["studentEmail"].toString(),
                            doc["status"].toString(),
                            doc["dateTime"].toString(),
                            doc["reason"].toString(),
                        )
                        if(doc["status"].toString() == Constants.OrderStatus.CANCEL.toString()){
                            order.cancelReason = doc["cancelReason"].toString()
                        }
                        orderList.add(order)
                    } else {
                        val order = OrderBankLoansIdentify(
                            doc.id,
                            doc["studentEmail"].toString(),
                            doc["status"].toString(),
                            doc["dateTime"].toString(),
                            doc["tuitionKind"].toString(),
                            doc["familyKind"].toString(),
                        )
                        if(doc["status"].toString() == Constants.OrderStatus.CANCEL.toString()){
                            order.cancelReason = doc["cancelReason"].toString()
                        }
                        orderList.add(order)
                    }
                }
                orders.value = orderList
            }
        }
        return orders
    }

}