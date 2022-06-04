package com.example.svbookmarket.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment.STYLE_NORMAL
import com.example.svbookmarket.activities.adapter.AddressAdapter
import com.example.svbookmarket.activities.common.SimpleCustomSnackbar
import com.example.svbookmarket.activities.model.UserDeliverAddress
import com.example.svbookmarket.activities.viewmodel.AddressViewModel
import com.example.svbookmarket.databinding.ActivityAddressBinding
import com.example.svbookmarket.R
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressActivity : AppCompatActivity(), OnCreateAddressListener, OnEditAddressListener,
    OnDeleteAddressListener,
    AddressAdapter.NotifyAddressSelectionChanged {
    companion object {
        const val CHANGE_ADDRESS = "CHANGE_ADDRESS"
        const val FROM_CART = "FROM_CART"
    }

    lateinit var binding: ActivityAddressBinding
    val viewmodel: AddressViewModel by viewModels()
    private val addressAdapter = AddressAdapter(mutableListOf(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val addressList = binding.adRecyclerview
        val newAddressBtn = binding.adNewAddress
        val editAddressBtn = binding.adEditAdress

        // if this activity has not been opened from cart activity, hide continue button
        if (intent.extras?.getBoolean(FROM_CART) != null)
            binding.adContinue.visibility = View.VISIBLE

        //on back
        binding.adBack.setOnClickListener { onBackPressed() }

        binding.adContinue.setOnClickListener { onBackPressed() }

        // show add address form listener
        newAddressBtn.setOnClickListener {
            showAddNewAddressDialog()
        }


        // show edit form listener
        editAddressBtn.setOnClickListener {
            showEditAddressDialog()
        }


        addressList.adapter = addressAdapter
        watchForDataChange()
    }

    private fun showAddNewAddressDialog() {
        val newAddressDialog = CreateAddressDialog(this)
        newAddressDialog.setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
        newAddressDialog.show(supportFragmentManager, "tag")
    }

    private fun showEditAddressDialog() {
        val editAddressDialog =
            viewmodel.selectedItem?.let { selectedItem ->
                EditAddressDialog(
                    this,
                    selectedItem,
                    this
                )
            }
        editAddressDialog?.setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
        editAddressDialog?.show(supportFragmentManager, "tag")
    }

    /**
     * watch for changes from database, then update ui
     */
    private fun watchForDataChange() {
        viewmodel.address.observe(this, { address ->

            if (address.size > 0) {
                // enable edit button
                changeEditButtonState(true)

                changeContinueToPaymentState(true)
                // add freshen items to address then notify adapter about changes
                // then show recyclerview
                addItemToRecyclerView(address)
                showRecyclerView()

            } else {
                changeContinueToPaymentState(false)
                // if no address, show empty notification
                showEmptyNotification()
                // disable edit button
                changeEditButtonState(false)
            }
        })
    }

    private fun changeEditButtonState(canEdit: Boolean) {
        binding.adEditAdress.apply {
            isEnabled = canEdit
            val textColor =
                if (canEdit) resources.getColor(R.color.blue_dark) else resources.getColor(R.color.disable)
            setTextColor(textColor)
        }
    }

    private fun changeContinueToPaymentState(canContinue: Boolean) {
        binding.adContinue.isClickable = canContinue
        binding.adContinue.backgroundTintList = if (canContinue)
            getColorStateList(R.color.green)
        else getColorStateList(R.color.disable)

    }

    private fun addItemToRecyclerView(address: MutableList<UserDeliverAddress>) {
        addressAdapter.addAddress(address)
    }

    private fun showEmptyNotification() {
        binding.adRecyclerview.visibility = View.GONE
        binding.adEmptyImage.visibility = View.VISIBLE
        binding.adEmptyText.visibility = View.VISIBLE
    }

    private fun showRecyclerView() {
        binding.adRecyclerview.visibility = View.VISIBLE
        binding.adEmptyImage.visibility = View.GONE
        binding.adEmptyText.visibility = View.GONE
    }


    override fun onCreateAddress(address: UserDeliverAddress) {
        viewmodel.addAddress(address)
        SimpleCustomSnackbar.make(
            binding.root,
            "Address added successful",
            Snackbar.LENGTH_SHORT,
            null,
            R.drawable.ic_thumb_up,
            null,
            ContextCompat.getColor(binding.root.context, R.color.green)
        )?.show()
    }

    override fun onAddressChange(old: UserDeliverAddress, new: UserDeliverAddress) {
        viewmodel.chageSelectState(old, new)
        viewmodel.selectedItem = new


    }

    override fun onEditAddress(address: UserDeliverAddress) {
        viewmodel.updateCurrentAddress(address)

    }

    override fun onDeleteAddress(address: UserDeliverAddress) {
        viewmodel.selectedItem = null
        viewmodel.deleteAddress(address)
        Log.i("concac1", " o delete func ${viewmodel.selectedItem }")
        viewmodel.getAddress()

//        SimpleCustomSnackbar.make(
//            binding.root,
//            "Address deleted",
//            Snackbar.LENGTH_SHORT,
//            null,
//            R.drawable.ic_thumb_up,
//            null,
//            ContextCompat.getColor(binding.root.context, R.color.green)
//        )?.show()

    }
}

interface OnCreateAddressListener {
    fun onCreateAddress(address: UserDeliverAddress)
}

interface OnEditAddressListener {
    fun onEditAddress(address: UserDeliverAddress)
}

interface OnDeleteAddressListener {
    fun onDeleteAddress(address: UserDeliverAddress)
}


