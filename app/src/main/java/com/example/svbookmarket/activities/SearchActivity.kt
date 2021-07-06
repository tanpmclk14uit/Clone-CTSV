package com.example.svbookmarket.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.data.FullBookList
import com.example.svbookmarket.activities.model.Book
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot


class SearchActivity : AppCompatActivity() {
    lateinit var suggestSearch: ListView
    lateinit var searchBar: SearchView
    private lateinit var adapter : ArrayAdapter<String>
    private var dataset : MutableList<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        suggestSearch = findViewById(R.id.rc_suggest_search)
        searchBar = findViewById(R.id.tb_searchView)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,  mutableListOf())

        for (i in 0 until FullBookList.getInstance().lstFullBook.size - 1) {
            dataset.add(FullBookList.getInstance().lstFullBook[i].Name!! )
        }

        suggestSearch.adapter = adapter

        //Click suggest search
        suggestSearch.setOnItemClickListener(
            AdapterView.OnItemClickListener { parent, view, position, id ->
                var Name: String = suggestSearch.getItemAtPosition(position).toString().split("\n")[0];
                Log.d("00000000000000", Name)
                for (book in FullBookList.getInstance().lstFullBook) {
                    if (book.Name == Name) {
                        ContextCompat.startActivity(parent.context, putBookIntoIntent(book), null);
                    }
                }
            }
        )
        //show keyboard on search bar click
        searchBar.setOnClickListener{
            searchBar.isIconified = false
        }

        //searching
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchBar.clearFocus()

                if (query == null)
                {
                    suggestSearch.visibility = View.INVISIBLE
                    return false
                }
                if(dataset.contains(query))
                {
                    adapter.filter.filter(query)
                    suggestSearch.visibility = View.VISIBLE
                }
                else
                {
                    Toast.makeText(applicationContext,"Item not found !", Toast.LENGTH_LONG).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != "") {
                    adapter.filter.filter(newText)
                    suggestSearch.visibility = View.VISIBLE
                }
                else
                {
                    suggestSearch.visibility = View.INVISIBLE
                }
                return false
            }

        })
        snipInDb()
    }

    private fun putBookIntoIntent(item: Book): Intent {
        val bundle = Bundle()
        bundle.putParcelable(Constants.ITEM, item)
        val i = Intent(this, ItemDetailActivity::class.java)
        i.putExtras(bundle)
        return i
    }

    fun snipInDb()
    {
        FirebaseFirestore.getInstance().collection("books").addSnapshotListener(object: EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.w(Constants.VMTAG, "Listen failed.", error)
                    return
                }
                adapter.clear()
                dataset = mutableListOf()
                for (i in 0 until FullBookList.getInstance().lstFullBook.size-1)
                {
                    dataset.add(FullBookList.getInstance().lstFullBook[i].Name!!)
                    adapter.add(FullBookList.getInstance().lstFullBook[i].Name!! + "\n" + "Selled by: "+ FullBookList.getInstance().lstFullBook[i].SalerName!!)
                }
                adapter.notifyDataSetChanged()
                var newString: String = searchBar.query.toString()
                searchBar.setQuery(searchBar.query.toString() + "a", false)
                searchBar.setQuery(newString, false)
            }
        })
    }



}