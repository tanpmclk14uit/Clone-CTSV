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
class WaitingForDeliveryViewModel @Inject constructor(private val orderRepository: OrderRepository) :
    ViewModel() {
    private var _waitingOrder = MutableLiveData<MutableList<Order>>()
    val waitingOrders get() = _waitingOrder
    private var _deliveringOrder = MutableLiveData<MutableList<Order>>()
    val deliveryOrder get() = _deliveringOrder
    private var _confirmedOrder = MutableLiveData<MutableList<Order>>()
    val confirmOrder get() = _confirmedOrder

    fun setDeliveringOrder(): MutableLiveData<MutableList<Order>> {
        orderRepository.getAllOrderFromCloudFireStore().addSnapshotListener { value, error ->
            if (error != null) {
                Log.w(Constants.VMTAG, "Listen failed.", error)
            } else {
                var orderList: MutableList<Order> = ArrayList()
                for (doc in value!!) {
                    if (doc["status"].toString() == Constants.OrderStatus.PRINTED.toString()) {
                        if (doc["kind"].toString() == Constants.OrderKind.GXNSV.toString()) {
                            val order = OrderStudentIdentify(
                                doc.id,
                                doc["studentEmail"].toString(),
                                doc["status"].toString(),
                                doc["dateTime"].toString(),
                                doc["reason"].toString(),
                            )
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
                            orderList.add(order)
                        }
                    }
                }
                deliveryOrder.value = orderList
            }
        }
        return deliveryOrder
    }

    fun setConfirmedOrder(): MutableLiveData<MutableList<Order>> {
        orderRepository.getAllOrderFromCloudFireStore().addSnapshotListener { value, error ->
            if (error != null) {
                Log.w(Constants.VMTAG, "Listen failed.", error)
            } else {
                val orderList: MutableList<Order> = ArrayList()
                for (doc in value!!) {
                    if (doc["status"].toString() == Constants.OrderStatus.CONFIRMED.toString()) {
                        if (doc["kind"].toString() == Constants.OrderKind.GXNSV.toString()) {
                            val order = OrderStudentIdentify(
                                doc.id,
                                doc["studentEmail"].toString(),
                                doc["status"].toString(),
                                doc["dateTime"].toString(),
                                doc["reason"].toString(),
                            )
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
                            orderList.add(order)
                        }
                    }
                }
                confirmOrder.value = orderList
            }
        }
        return confirmOrder
    }

    private fun getFormatDate(date: Date): String {
        val sdf = SimpleDateFormat("HH:mm:ss dd-MM-yyyy ")
        return sdf.format(date)
    }

    fun setWaitingOrder(): MutableLiveData<MutableList<Order>> {
        orderRepository.getAllOrderFromCloudFireStore().addSnapshotListener { value, error ->
            if (error != null) {
                Log.w(Constants.VMTAG, "Listen failed.", error)
            } else {
                var orderList: MutableList<Order> = ArrayList()
                for (doc in value!!) {
                    if (doc["status"].toString() == Constants.OrderStatus.WAITING.toString()) {
                        if (doc["kind"].toString() == Constants.OrderKind.GXNSV.toString()) {
                            val order = OrderStudentIdentify(
                                doc.id,
                                doc["studentEmail"].toString(),
                                doc["status"].toString(),
                                doc["dateTime"].toString(),
                                doc["reason"].toString(),
                            )
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
                            orderList.add(order)
                        }
                    }
                }
                waitingOrders.value = orderList
            }
        }
        return waitingOrders
    }


}