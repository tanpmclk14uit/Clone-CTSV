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
class WaitingForDeliveryViewModel @Inject constructor(private val orderRepository: OrderRepository) :
    ViewModel() {
    private var _waitingOrder = MutableLiveData<MutableList<Order>>()
    val waitingOrders get() = _waitingOrder
    private var _deliveringOrder = MutableLiveData<MutableList<Order>>()
    val deliveryOrder get() = _deliveringOrder
    private var _confirmedOrder = MutableLiveData<MutableList<Order>>()
    val confirmOrder get() = _confirmedOrder

//    fun setDeliveringOrder(): MutableLiveData<MutableList<Order>> {
//        orderRepository.getAllOrderFromCloudFireStore().addSnapshotListener { value, error ->
//            if (error != null) {
//                Log.w(Constants.VMTAG, "Listen failed.", error)
//            } else {
//                var orderList: MutableList<Order> = ArrayList()
//                for (doc in value!!) {
//                    val order = Order()
//                    if(doc["status"].toString() == "DELIVERING"){
//                        order.id = doc.id
//                        val timeStamp = doc["dateTime"] as Timestamp
//                        order.dateTime = getFormatDate(timeStamp.toDate())
//                        order.status = doc["status"].toString()
//                        orderList.add(order)
//                    }
//                }
//                deliveryOrder.value = orderList
//            }
//        }
//        return deliveryOrder
//    }
//
//    fun setConfirmedOrder(): MutableLiveData<MutableList<Order>> {
//        orderRepository.getAllOrderFromCloudFireStore().addSnapshotListener { value, error ->
//            if (error != null) {
//                Log.w(Constants.VMTAG, "Listen failed.", error)
//            } else {
//                var orderList: MutableList<Order> = ArrayList()
//                for (doc in value!!) {
//                    if(doc["status"].toString() == "CONFIRMED"){
//                        val order = Order()
//                        order.id = doc.id
//                        val timeStamp = doc["dateTime"] as Timestamp
//                        order.dateTime = getFormatDate(timeStamp.toDate())
//                        order.status = doc["status"].toString()
//                        orderList.add(order)
//                    }
//                }
//                confirmOrder.value = orderList
//            }
//        }
//        return confirmOrder
//    }
//    private fun getFormatDate(date: Date):String{
//        val sdf = SimpleDateFormat("HH:mm:ss dd-MM-yyyy ")
//        return sdf.format(date)
//    }
//
//    fun setWaitingOrder(): MutableLiveData<MutableList<Order>> {
//        orderRepository.getAllOrderFromCloudFireStore().addSnapshotListener { value, error ->
//            if (error != null) {
//                Log.w(Constants.VMTAG, "Listen failed.", error)
//            } else {
//                var orderList: MutableList<Order> = ArrayList()
//                for (doc in value!!) {
//                    if(doc["status"].toString() == "WAITING"){
//                        val order = Order()
//                        order.id = doc.id
//                        val timeStamp = doc["dateTime"] as Timestamp
//                        order.dateTime = getFormatDate(timeStamp.toDate())
//                        order.status = doc["status"].toString()
//                        orderList.add(order)
//                    }
//                }
//                waitingOrders.value = orderList
//            }
//        }
//        return waitingOrders
//    }


}