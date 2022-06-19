package com.example.svbookmarket.activities.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.data.OrderRepository
import com.example.svbookmarket.activities.model.Book
import com.example.svbookmarket.activities.model.Order
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
        //_order = getAllOrder()
    }

    private fun getFormatDate(date: Date):String{
        val sdf = SimpleDateFormat("HH:mm:ss dd-MM-yyyy ")
        return sdf.format(date)
    }


//    private fun getAllOrder(): MutableLiveData<MutableList<Order>> {
//        orderRepository.getAllOrderFromCloudFireStore().addSnapshotListener { value, error ->
//            if (error != null) {
//                Log.w(Constants.VMTAG, "Listen failed.", error)
//            } else {
//                var orderList: MutableList<Order> = ArrayList()
//                for (doc in value!!) {
//                    val order = Order()
//                    order.id = doc.id
//                    val timeStamp = doc["dateTime"] as Timestamp
//                    order.dateTime = getFormatDate(timeStamp.toDate())
//                    order.status = doc["status"].toString()
//                    orderList.add(order)
//                }
//                orders.value = orderList
//            }
//        }
//        return orders
//    }
//
}