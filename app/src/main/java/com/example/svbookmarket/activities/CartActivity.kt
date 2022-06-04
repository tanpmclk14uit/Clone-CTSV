package com.example.svbookmarket.activities

import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.adapter.CartItemAdapter
import com.example.svbookmarket.activities.model.Cart
import com.example.svbookmarket.activities.viewmodel.CartViewModel
import com.example.svbookmarket.databinding.ActivityCartBinding
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

@AndroidEntryPoint
class CartActivity : AppCompatActivity(), CartItemAdapter.OnButtonClickListener {
    lateinit var binding: ActivityCartBinding
    private val viewModel: CartViewModel by viewModels()
    var cartAdapter: CartItemAdapter = CartItemAdapter(this, mutableListOf())

    lateinit var dataCart: MutableList<Cart>
    var deleteItem: View? = null

    //variable to save deleted item, later is used for redo action
    lateinit var delete: Cart


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        // set activity to display here
        setContentView(binding.root)
        viewModel.cartItem.observe(this, changeObserver)

        binding.rcCardList.apply {
            layoutManager =
                LinearLayoutManager(this@CartActivity)
            setHasFixedSize(true)
            adapter = cartAdapter
            touchHelper.attachToRecyclerView(this)
        }
        binding.backButton.setOnClickListener { onBackPressed() }
        binding.ctCheckout.setOnClickListener { navigateToCheckout() }
        binding.ctAddItem.setOnClickListener { onBackPressed() }
    }

    private val changeObserver = Observer<MutableList<Cart>> { value ->
        // if have any item in cart. show cart recycler view, otherwise show empty notification
        if (value.size > 0) {
            //show guide text
                binding.ctGuide.visibility = View.VISIBLE
            value?.let {
                dataCart = value
                cartAdapter.onChange(value)
                cartAdapter.notifyDataSetChanged()
                showCartList()

                val selectedItem = value.find { it.isChose }
                if (selectedItem != null)
                    setButtonColor(R.color.green, true)
                else
                    setButtonColor(R.color.disable, false)
            }
        } else {
            //hide guide text
            binding.ctGuide.visibility = View.GONE
            setButtonColor(R.color.disable, false)
            showEmptyNotification()
        }

    }

    private fun showEmptyNotification() {
        binding.rcCardList.visibility = GONE
        binding.ctChildContainer.visibility = View.VISIBLE
    }

    private fun showCartList() {
        binding.rcCardList.visibility = View.VISIBLE
        binding.ctChildContainer.visibility = GONE
    }

    private fun setButtonColor(color: Int, clickable: Boolean) {
        binding.ctCheckout.backgroundTintList = getColorStateList(color)
        binding.ctCheckout.isClickable = clickable
    }

    private fun navigateToCheckout() {
        val intent =
            Intent(this, CheckoutActivity::class.java)
        startActivity(intent)
    }



    override fun onButtonClick(id: String, plusOrMinus: Boolean) {
        viewModel.updateQuantity(id, plusOrMinus)
    }

    override fun onItemClick(id: String, isChose: Boolean) {
        viewModel.isChoseChange(id, isChose)
    }

    private var touchHelper = ItemTouchHelper(
        object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: ViewHolder, target: ViewHolder
            ): Boolean {
                val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition
                // move item in `fromPos` to `toPos` in adapter.
                return false // true if moved, false otherwise
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                // remove from adapter
                val itemPos = viewHolder.adapterPosition
                delete = dataCart[itemPos]
                deleteItem = viewHolder.itemView
                // TODO: add delete on db
                viewModel.deleteCart(dataCart[itemPos].id)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addBackgroundColor(
                        ContextCompat.getColor(
                            this@CartActivity,
                            R.color.danger_light
                        )
                    )
                    .addActionIcon(R.drawable.ic_trashbin).setIconHorizontalMargin(50)
                    .create()
                    .decorate()
            }
        })


}




