package com.example.svbookmarket.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.svbookmarket.R

class MenuActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)
        findViewById<TextView>(R.id.m_home).setOnClickListener{startIntent("home")}
        findViewById<TextView>(R.id.m_profile).setOnClickListener{startIntent("profile")}
        findViewById<TextView>(R.id.m_cart).setOnClickListener{startIntent("cart")}
        findViewById<TextView>(R.id.m_item).setOnClickListener{startIntent("item")}
        findViewById<TextView>(R.id.m_checkout).setOnClickListener{startIntent("checkout")}
    findViewById<TextView>(R.id.m_address).setOnClickListener{startIntent("address")}

    }

    private fun startIntent(type:String){
        val intent = when(type){
            "home"-> Intent(this, HomeActivity::class.java)
            "profile"-> Intent(this, ProfileActivity::class.java)
            "cart"-> Intent(this, CartActivity::class.java)
            "address"-> Intent(this, AddressActivity::class.java)
            else-> Intent(this, ItemDetailActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}