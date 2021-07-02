package com.example.svbookmarket.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.svbookmarket.R
import com.example.svbookmarket.R.*
import com.example.svbookmarket.activities.adapter.CheckoutAdapter
import com.example.svbookmarket.activities.model.Cart
import com.example.svbookmarket.activities.model.UserDeliverAddress
import com.example.svbookmarket.activities.viewmodel.CheckoutViewModel
import com.example.svbookmarket.databinding.ActivityCheckoutBinding
import com.example.svbookmarket.databinding.DialogEmptyNotiBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutActivity : AppCompatActivity() {

    private val viewModel: CheckoutViewModel by viewModels()
    private lateinit var binding: ActivityCheckoutBinding
    lateinit var dataset: MutableList<Cart>
    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private var checkoutAdapter: CheckoutAdapter = CheckoutAdapter(this, mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        materialAlertDialogBuilder = MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)

        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        displayAddress()
        setCheckoutAdapter()
        setCheckoutButton()
        setupNavigation()
        watchForDataChange()
    }

    private fun watchForDataChange() {
        viewModel.checkoutItem.observe(this, changeObserver)
        viewModel.deliverAddress.observe(this, changeAddress)
    }

    private fun setupNavigation() {
        binding.coBackButton.setOnClickListener { onBackPressed() }
        binding.ctChangeLocation.setOnClickListener { navigateToAddress() }
    }

    private fun setCheckoutButton() {
        binding.coCheckout.setOnClickListener {
            if (viewModel.deliverAddress.value?.fullName != null && viewModel.deliverAddress.value?.addressLane != null &&
                viewModel.deliverAddress.value?.city != null && viewModel.deliverAddress.value?.district != null &&
                viewModel.deliverAddress.value?.phoneNumber != null && viewModel.deliverAddress.value?.fullName.toString() != "" &&
                viewModel.deliverAddress.value?.addressLane.toString() != "" && viewModel.deliverAddress.value?.city.toString() != "" &&
                viewModel.deliverAddress.value?.district.toString() != "" && viewModel.deliverAddress.value?.phoneNumber.toString() != "") {
                launchReviewDialog()
            } else
                launchEmptyAddressNotificationDialog()
        }

        binding.coBackButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun launchReviewDialog() {
        val buyReviewDialog = CheckoutDialog()
        buyReviewDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
        buyReviewDialog.show(supportFragmentManager, "tag")
    }

    private fun setCheckoutAdapter() {
        binding.rcCheckout.apply {
            layoutManager =
                LinearLayoutManager(this@CheckoutActivity)
            setHasFixedSize(true)
            adapter = checkoutAdapter
        }
    }

    private fun displayAddress() {
        val s = StringBuilder("")
        viewModel.deliverAddress.value?.let {
            s.append("${it.fullName}\n")
                .append("${ it.phoneNumber}\n")
                .append(it.addressLane)
                .append(", ")
                .append(it.district)
                .append(", ")
                .append(it.city)
            binding.tvAddress.text = s
        }

    }


    private fun navigateToAddress() {
        val i = Intent(this, AddressActivity::class.java).putExtra(
            AddressActivity.FROM_CART, true
        )
        startActivity(i)
    }

    private val changeObserver = Observer<MutableList<Cart>> { value ->
        value?.let {
            dataset = value
            checkoutAdapter.onChange(value)
            checkoutAdapter.notifyDataSetChanged()
        }
    }

    private val changeAddress = Observer<UserDeliverAddress> { value ->
        value?.let {
            displayAddress()
        }
    }

    private fun launchEmptyAddressNotificationDialog() {

        val dialogView = DialogEmptyNotiBinding.inflate(layoutInflater).root
        // Building the Alert dialog using materialAlertDialogBuilder instance
        materialAlertDialogBuilder.setView(dialogView)
            .setTitle("Empty address")

            .setPositiveButton(R.string.add_an_address) { dialog, _ ->
                val intent = Intent(this, AddressActivity::class.java).putExtra(
                    AddressActivity.FROM_CART,
                    true
                )
                startActivity(intent)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
