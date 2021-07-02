package com.example.svbookmarket.activities

import CurrentUserInfo
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.common.AppUtil
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.data.FullBookList
import com.example.svbookmarket.activities.model.AppAccount
import com.example.svbookmarket.activities.model.Cart
import com.example.svbookmarket.activities.model.UserDeliverAddress
import com.example.svbookmarket.databinding.BillingBlockBinding
import com.example.svbookmarket.databinding.ItemBillingBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.api.Billing
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

@AndroidEntryPoint
class CheckoutDialog : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_checkout, container, false)
        return view
    }

    private var lstSalerID: MutableList<String> = mutableListOf()
    private  var lstSalerFullName: MutableList<String> = mutableListOf()
    private var items: MutableList<Cart> = mutableListOf()
    private var sum: Long = 0
    private var billingAdapter: BillAdapter =  BillAdapter(items, lstSalerID, lstSalerFullName)
    lateinit var tvName: TextView
    lateinit var tvAddress: TextView
    lateinit var tvPhonenumber: TextView
    lateinit var billingItemList: RecyclerView
    lateinit var btn_buy: MaterialButton
    private var deliverAddress: UserDeliverAddress = UserDeliverAddress()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        btn_buy = view.findViewById(R.id.mdco_checkout)
//        tvSum = view.findViewById(R.id.tv_sum)
        tvName = view.findViewById(R.id.tv_Name)
        tvAddress = view.findViewById(R.id.tv_address)
        tvPhonenumber = view.findViewById(R.id.tv_phonenumber)

//        tvSum.text = sum.toString() + " đ"


        billingItemList = view.findViewById(R.id.rc_Billing)
        billingItemList.layoutManager = LinearLayoutManager(context)
        billingItemList.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL,
            )
        )
        billingItemList.adapter = billingAdapter

        btn_buy.setOnClickListener {
            var num: Int = 0
            for (i in 0 until CurrentUserInfo.getInstance().lstUserCart.size) {
                if (CurrentUserInfo.getInstance().lstUserCart[i].isChose == true) {
                    num += 1
                }
            }
            if (num != 0) {
                if (checkIfComeFirst()) {
                    moveToOrder(
                        CurrentUserInfo.getInstance().currentProfile,
                        CurrentUserInfo.getInstance().lstUserCart,
                        CurrentUserInfo.getInstance().lstUserDeliverAddress
                    )
                    startActivity(Intent(context,ConfirmationActivity::class.java))
                } else {
                    val toast: Toast = Toast.makeText(context,"messenger",Toast.LENGTH_SHORT)
                    toast.setText("Not enough quantity in store")
                    toast.show()
                    val handler = Handler()
                    handler.postDelayed({ toast.cancel() }, 500)
                }
            } else {
                val toast: Toast = Toast.makeText(context,"messenger",Toast.LENGTH_SHORT)
                toast.setText("Have nothing to checkout")
                toast.show()
                val handler = Handler()
                handler.postDelayed({ toast.cancel() }, 500)
            }
        }

        loadCheckoutData()
        loadDeliverAddress()
    }


    private fun loadDeliverAddress() {
        FirebaseFirestore.getInstance().collection("accounts")
            .document(CurrentUserInfo.getInstance().currentProfile.email)
            .collection("userDeliverAddresses").addSnapshotListener(object :
                EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.w(Constants.VMTAG, "Listen failed.", error)
                        return
                    }

                    val itemDeliverAddress: UserDeliverAddress
                    for (doc in value!!) {
                        if (doc.data["chose"].toString() == "true") {
                            var item = UserDeliverAddress(
                                doc.id,
                                doc.data["fullName"].toString(),
                                doc.data["phoneNumber"].toString(),
                                doc.data["addressLane"].toString(),
                                doc.data["district"].toString(),
                                doc.data["city"].toString(),
                                true
                            )
                            itemDeliverAddress = item
                            deliverAddress = itemDeliverAddress
                            tvName.text = deliverAddress.fullName
                            tvPhonenumber.text = deliverAddress.phoneNumber
                            tvAddress.text =
                                deliverAddress.addressLane + ", " + deliverAddress.district + ", " + deliverAddress.city
                            break
                        }
                    }

                }

            })
    }

    fun moveToOrder(
        user: AppAccount,
        lstCheckout: MutableList<Cart>,
        lstAddress: MutableList<UserDeliverAddress>
    ) {
        for (i in 0 until lstAddress.size) {
            if (lstAddress[i].chose) {
                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        moveToUserOrDer(user, lstCheckout, lstAddress[i])
                    }
                }
                break
            }
        }
    }
    fun getCurrentDate():String{
        val sdf = SimpleDateFormat("HH:mm:ss dd-MM-yyyy ")
        return sdf.format(Date())
    }

    suspend fun moveToUserOrDer(
        user: AppAccount,
        listNeedToMove: MutableList<Cart>,
        deliverAddress: UserDeliverAddress
    ) {
        val time = Date()
        val mapOfAddress = hashMapOf<String, Any>(
            "addressId" to deliverAddress.id,
            "addressLane" to deliverAddress.addressLane,
            "city" to deliverAddress.city,
            "district" to deliverAddress.district,
            "fullName" to deliverAddress.fullName,
            "phoneNumber" to deliverAddress.phoneNumber,
            "status" to Constants.TRANSACTION.WAITING,
            "dateTime" to time,
            "totalPrince" to sum,
            "user" to AppUtil.currentUser,
            "userId" to AppUtil.currentAccount.email
        )

//        val newOrderId: String =
//            FirebaseFirestore.getInstance().collection("accounts").document(user.email)
//                .collection("userOrder").add(mapOfAddress).await().get().await().id

        var lstIdOnMove: MutableList<String> = mutableListOf()
        var lstNumOnMove: MutableList<Int> = mutableListOf()
        var lstSaler: MutableList<String> = mutableListOf()
        var lstSalerName: MutableList<String> = mutableListOf()
        for (i in 0 until listNeedToMove.size) {
            if (listNeedToMove[i].isChose) {
                if (!lstSaler.contains(listNeedToMove[i].saler))
                {
                    lstSaler.add(listNeedToMove[i].saler)
                    lstSalerName.add(listNeedToMove[i].salerName)
                    onMoveForDifferenceSaler(user, listNeedToMove[i].saler, listNeedToMove,mapOfAddress)
                }
                lstIdOnMove.add(listNeedToMove[i].id)
                lstNumOnMove.add(listNeedToMove[i].numbers)
//                val mapOfOrder = hashMapOf<String, Any>(
//                    "Quantity" to listNeedToMove[i].numbers,
//                    "author" to listNeedToMove[i].author,
//                    "image" to listNeedToMove[i].imgUrl,
//                    "price" to listNeedToMove[i].price,
//                    "title" to listNeedToMove[i].name,
//                    "saler" to listNeedToMove[i].saler,
//                    "salerName" to listNeedToMove[i].salerName
//                )
//                FirebaseFirestore.getInstance().collection("accounts").document(user.email)
//                    .collection("userOrder").document(newOrderId).collection("books")
//                    .document(listNeedToMove[i].id).set(mapOfOrder)
            }
        }

        for (i in 0 until lstIdOnMove.size) {
            FirebaseFirestore.getInstance().collection("accounts").document(user.email)
                .collection("userCart").document(lstIdOnMove[i]).delete()
            FirebaseFirestore.getInstance().collection("books").document(lstIdOnMove[i])
                .update("rate", FieldValue.increment(lstNumOnMove[i].toDouble()))
            FirebaseFirestore.getInstance().collection("books").document(lstIdOnMove[i])
                .update("Counts", FieldValue.increment(-lstNumOnMove[i].toDouble()))
        }
    }

    suspend fun onMoveForDifferenceSaler(user: AppAccount, Saler: String,
                                 listNeedToMove: MutableList<Cart>, mapOfAddress: MutableMap<String, Any>)
    {
        val newOrderId: String =
            FirebaseFirestore.getInstance().collection("accounts").document(user.email)
                .collection("userOrder").add(mapOfAddress).await().get().await().id
        for (i in 0 until listNeedToMove.size) {
                if (listNeedToMove[i].saler == Saler)
                {
//                    var lstIdOnMove: MutableList<String> = mutableListOf()
//                    var lstNumOnMove: MutableList<Int> = mutableListOf()
//                    lstIdOnMove.add(listNeedToMove[i].id)
//                    lstNumOnMove.add(listNeedToMove[i].numbers)
                    val mapOfOrder = hashMapOf<String, Any>(
                        "Quantity" to listNeedToMove[i].numbers,
                        "author" to listNeedToMove[i].author,
                        "image" to listNeedToMove[i].imgUrl,
                        "price" to listNeedToMove[i].price,
                        "title" to listNeedToMove[i].name,
                        "saler" to listNeedToMove[i].saler,
                        "salerName" to listNeedToMove[i].salerName
                    )
                    FirebaseFirestore.getInstance().collection("accounts").document(user.email)
                        .collection("userOrder").document(newOrderId).collection("books")
                        .document(listNeedToMove[i].id).set(mapOfOrder)
                }
            }
    }

    fun checkIfComeFirst(): Boolean {
        for (i in 0 until CurrentUserInfo.getInstance().lstUserCart.size) {
            for (j in 0 until FullBookList.getInstance().lstFullBook.size) {
                if (CurrentUserInfo.getInstance().lstUserCart[i].id == FullBookList.getInstance().lstFullBook[j].id &&
                    FullBookList.getInstance().lstFullBook[j].Counts < CurrentUserInfo.getInstance().lstUserCart[i].numbers
                ) {
                    return false
                }
            }
        }
        return true
    }

    private fun loadCheckoutData() {
        FirebaseFirestore.getInstance().collection("accounts")
            .document(CurrentUserInfo.getInstance().currentProfile.email).collection("userCart")
            .addSnapshotListener(object :
                EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.w(Constants.VMTAG, "Listen failed.", error)
                        return
                    }

                    val checkoutList: MutableList<Cart> = ArrayList()
                    for (doc in value!!) {
                        if (doc.data["isChose"].toString() == "true") {
                            var item = Cart(
                                "",
                                doc.data["image"].toString(),
                                doc.data["title"].toString(),
                                doc.data["author"].toString(),
                                doc.data["Quantity"].toString().toDouble().roundToInt(),
                                doc.data["price"].toString().toLong(),
                                doc.data["saler"].toString(),
                                doc.data["salerName"].toString(),
                                true
                            )
                            item.id = doc.id
                            checkoutList.add(item)
                        }
                    }
                    items = checkoutList
                    for (i in 0 until  items.size)
                    {
                        if (!lstSalerID.contains(items[i].saler))
                        {
                            lstSalerID.add(items[i].saler)
                            lstSalerFullName.add(items[i].salerName)
                        }
                    }
                    sum = 0
                    for (item in items) {
                        sum += item.price * item.numbers
                    }
                    billingAdapter.onChange(items, lstSalerID, lstSalerFullName)
                    billingAdapter.notifyDataSetChanged()
                }

            })
    }

    private inner class ViewHolder internal constructor(binding: ItemBillingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        internal val itemName = binding.itName
        val itemPrice = binding.itPrice
    }

    private inner class ItemAdapter internal constructor(private var billingItem: MutableList<Cart>) :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            return ViewHolder(
                ItemBillingBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

        }

        fun onChange(newItems: MutableList<Cart>) {
            billingItem = newItems
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemPrice.text =
                (billingItem[position].price * billingItem[position].numbers).toString() + " đ"
            holder.itemName.text = billingItem[position].name
            Log.d("modal", "created")

        }

        override fun getItemCount(): Int {
            return billingItem.size
        }
    }


    private inner class BillingViewHolder internal constructor(binding: BillingBlockBinding) :
        RecyclerView.ViewHolder(binding.root) {
        internal var Saler = binding.idSalerName
        var Sum = binding.tvSum
        var rcBilling = binding.mdcoItemBill
    }
    private inner class BillAdapter internal constructor(private var billingItem: MutableList<Cart>,
                                                         private var lstSaler: MutableList<String>,
                                                         private var lstSalerName: MutableList<String>) :
        RecyclerView.Adapter<BillingViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingViewHolder {

            return BillingViewHolder(
                BillingBlockBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

        }

        fun onChange(newItems: MutableList<Cart>, newSaler: MutableList<String>, newSalerName: MutableList<String>) {
            billingItem = newItems
            lstSaler = newSaler
            lstSalerName = newSalerName
        }

        override fun onBindViewHolder(holder: BillingViewHolder, position: Int) {
            holder.Saler.text = lstSalerName[position]
            var lstCartOfSaler: MutableList<Cart> = mutableListOf()
            var sum: Long = 0
            for (i in 0 until  billingItem.size)
            {
                if (billingItem[i].saler == lstSaler[position])
                {
                    lstCartOfSaler.add(billingItem[i])
                    sum += billingItem[i].price * billingItem[i].numbers
                }
            }
            holder.Sum.text = sum.toString() + " đ"
            var billItemAdapter: ItemAdapter = ItemAdapter(lstCartOfSaler)
            holder.rcBilling.layoutManager = LinearLayoutManager(context)
            holder.rcBilling.adapter = billItemAdapter
        }

        override fun getItemCount(): Int {
            return lstSaler.size
        }
    }
}