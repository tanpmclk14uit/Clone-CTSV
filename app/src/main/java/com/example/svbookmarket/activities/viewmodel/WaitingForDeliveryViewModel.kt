package com.example.svbookmarket.activities.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.data.OrderRepository
import com.example.svbookmarket.activities.model.Cart
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

    private fun setBillingItem(docID: String, mutableLiveData: MutableLiveData<MutableList<Order>>) {
        orderRepository.getAllBillingIem(docID).addSnapshotListener { value, error ->
            if (error != null) {
                Log.w(Constants.VMTAG, "Listen failed.", error)
            } else {
                var billingList: ArrayList<Cart> = ArrayList()
                for (doc in value!!) {
                    var cart: Cart = Cart()
                    cart.numbers = doc["Quantity"].toString().toDouble().roundToInt()
                    cart.name = doc["title"].toString()
                    cart.price = doc["price"].toString().toLong()
                    cart.id = doc.id
                    billingList.add(cart)
                }
                var orderList: MutableList<Order> = ArrayList()
                for(order in mutableLiveData.value!!){
                    if(order.id == docID){
                        order.listbooks = billingList
                    }
                    orderList.add(order)
                }
                mutableLiveData.value = orderList
            }
        }
    }
    fun setDeliveringOrder(): MutableLiveData<MutableList<Order>> {
        orderRepository.getAllOrderFromCloudFireStore().addSnapshotListener { value, error ->
            if (error != null) {
                Log.w(Constants.VMTAG, "Listen failed.", error)
            } else {
                var orderList: MutableList<Order> = ArrayList()
                for (doc in value!!) {
                    val order = Order()
                    if(doc["status"].toString() == "DELIVERING"){
                        order.id = doc.id
                        val timeStamp = doc["dateTime"] as Timestamp
                        order.dateTime = getFormatDate(timeStamp.toDate())
                        order.status = doc["status"].toString()
                        order.totalPrince = doc["totalPrince"].toString()
                        order.userDeliverAddress.addressLane = doc["addressLane"].toString()
                        order.userDeliverAddress.fullName = doc["fullName"].toString()
                        order.userDeliverAddress.phoneNumber = doc["phoneNumber"].toString()
                        order.userDeliverAddress.city = doc["city"].toString()
                        order.userDeliverAddress.district = doc["district"].toString()
                        order.seller = doc["salerName"].toString()
                        order.sellerId = doc["Saler"].toString()
                        setBillingItem( order.id, deliveryOrder )
                        orderList.add(order)
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
                var orderList: MutableList<Order> = ArrayList()
                for (doc in value!!) {
                    if(doc["status"].toString() == "CONFIRMED"){
                        val order = Order()
                        order.id = doc.id
                        val timeStamp = doc["dateTime"] as Timestamp
                        order.dateTime = getFormatDate(timeStamp.toDate())
                        order.status = doc["status"].toString()
                        order.totalPrince = doc["totalPrince"].toString()
                        order.userDeliverAddress.addressLane = doc["addressLane"].toString()
                        order.userDeliverAddress.fullName = doc["fullName"].toString()
                        order.userDeliverAddress.phoneNumber = doc["phoneNumber"].toString()
                        order.userDeliverAddress.city = doc["city"].toString()
                        order.userDeliverAddress.district = doc["district"].toString()
                        order.seller = doc["salerName"].toString()
                        order.sellerId = doc["Saler"].toString()
                        setBillingItem( order.id, confirmOrder )
                        orderList.add(order)
                    }
                }
                confirmOrder.value = orderList
            }
        }
        return confirmOrder
    }
    private fun getFormatDate(date: Date):String{
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
                    if(doc["status"].toString() == "WAITING"){
                        val order = Order()
                        order.id = doc.id
                        val timeStamp = doc["dateTime"] as Timestamp
                        order.dateTime = getFormatDate(timeStamp.toDate())
                        order.status = doc["status"].toString()
                        order.totalPrince = doc["totalPrince"].toString()
                        order.userDeliverAddress.addressLane = doc["addressLane"].toString()
                        order.userDeliverAddress.fullName = doc["fullName"].toString()
                        order.userDeliverAddress.phoneNumber = doc["phoneNumber"].toString()
                        order.userDeliverAddress.city = doc["city"].toString()
                        order.userDeliverAddress.district = doc["district"].toString()
                        order.seller = doc["salerName"].toString()
                        order.sellerId = doc["Saler"].toString()
                        setBillingItem( order.id, waitingOrders )
                        orderList.add(order)
                    }
                }
                waitingOrders.value = orderList
            }
        }
        return waitingOrders
    }


}