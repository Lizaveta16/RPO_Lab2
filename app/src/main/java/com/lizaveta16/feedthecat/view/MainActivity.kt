package com.lizaveta16.feedthecat.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.lizaveta16.feedthecat.R
import com.lizaveta16.feedthecat.databinding.ActivityMainBinding
import com.lizaveta16.feedthecat.model.Jewelry
import com.lizaveta16.feedthecat.rvadapter.JewelryAdapter
import com.lizaveta16.feedthecat.utils.Constants
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth
    lateinit var jewelriesDBRef: DatabaseReference

    override fun onStart() {
        super.onStart()

        if (!isShowAllJewerlies) {
            binding.resetFiltersButton.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContext(this)

        auth = Firebase.auth
        jewelriesDBRef = FirebaseDatabase.getInstance().reference.child("Jewelries")

        jewelriesDBRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isShowAllJewerlies) {
                    jewelryList.clear()
                    for (s in snapshot.children) {
                        val user = s.getValue(Jewelry::class.java)
                        if (user != null) {
                            jewelryList.add(user)
                        }
                    }
                    fullCoursesList.clear()
                    fullCoursesList.addAll(jewelryList)
                    jewelryAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        init()
    }

    private fun init() {
        setJewelryRecycler()

        binding.apply {
            addJewelryBut.setOnClickListener {
                val intent = Intent(this@MainActivity, AddEditJewelryActivity::class.java)
                startActivity(intent)
            }
            filtersButton.setOnClickListener {
                val intent = Intent(this@MainActivity, FiltersActivity::class.java)
                startActivity(intent)
            }

            resetFiltersButton.setOnClickListener {
                isShowAllJewerlies = true
                resetFiltersButton.visibility = View.GONE
                jewelryList.clear()
                jewelryList.addAll(fullCoursesList)
                jewelryAdapter.notifyDataSetChanged()
            }
        }

    }

    private fun setJewelryRecycler() {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        type = Constants.LIST
        jewelryAdapter.setType(Constants.LIST)
        binding.jewelryRecycler.layoutManager = layoutManager
        binding.jewelryRecycler.adapter = jewelryAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.signOut -> {
                auth.signOut()
                val devIntent = Intent(this@MainActivity, SignInActivity::class.java)
                startActivity(devIntent)
                finish()
            }
            R.id.list -> {
                if (type == Constants.GRID) {
                    binding.jewelryRecycler.layoutManager =
                        LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                    type = Constants.LIST
                    item.icon = ContextCompat.getDrawable(this, R.drawable.ic_grid)
                } else {
                    binding.jewelryRecycler.layoutManager =
                        GridLayoutManager(applicationContext, 2)
                    type = Constants.GRID
                    item.icon = ContextCompat.getDrawable(this, R.drawable.ic_list)
                }
                jewelryAdapter.setType(type)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        private lateinit var context: Context
        var jewelryList = mutableListOf<Jewelry>()

        lateinit var jewelryAdapter: JewelryAdapter
        var fullCoursesList = mutableListOf<Jewelry>()
        var isShowAllJewerlies = true
        var type = Constants.LIST

        fun showJewerliesByFilters(
            categories: List<String>,
            sizeFrom: Double,
            sizeTo: Double,
            priceFrom: Double,
            priceTo: Double
        ) {
            isShowAllJewerlies = false
            jewelryList.clear()
            var filterCourses = mutableListOf<Jewelry>()

            for (c in fullCoursesList) {
                for (category in categories) {
                    if (c.category == category && c.price >= priceFrom && c.price <= priceTo && c.size >= sizeFrom && c.size <= sizeTo) {
                        filterCourses.add(c)
                        break
                    }
                }
            }
            jewelryList.clear()
            jewelryList.addAll(filterCourses)
            jewelryAdapter.notifyDataSetChanged()
        }

        fun setContext(ctx: Context) {
            context = ctx
            jewelryAdapter = JewelryAdapter(context, jewelryList, type)
        }
    }
}