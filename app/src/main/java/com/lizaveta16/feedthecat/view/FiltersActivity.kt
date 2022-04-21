package com.lizaveta16.feedthecat.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lizaveta16.feedthecat.databinding.ActivityFiltersBinding
import com.lizaveta16.feedthecat.rvadapter.FilterAdapter
import com.lizaveta16.feedthecat.utils.Constants

class FiltersActivity : AppCompatActivity() {

    private var categories = mutableListOf<String>()
    private  var sizeTo: Double = Constants.SIZES.first()
    private var sizeFrom: Double = Constants.SIZES.last()
    private var priceFrom: Double = 0.0
    private var priceTo: Double = 20000.0
    private lateinit var categoryAdapter : FilterAdapter

    lateinit var binding : ActivityFiltersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFiltersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSizesSpinners()

        setCategoryRecycler(Constants.CATEGORIES.values.toMutableList())
        FilterAdapter.categoryList.clear()

        binding.filterButton.setOnClickListener {
           validateFilterData()
        }
    }

    private fun validateFilterData() {
        if(binding.priceFromET.text.isNotEmpty()){
            priceFrom = binding.priceFromET.text.toString().toDouble()
        }
        if(binding.priceToET.text.isNotEmpty()){
            priceTo = binding.priceToET.text.toString().toDouble()
        }
        sizeFrom = binding.spinnerSizeFrom.selectedItem.toString().toDouble()
        sizeTo= binding.spinnerSizeTo.selectedItem.toString().toDouble()
        categories = FilterAdapter.categoryList
            //storeProductInfo()

        if(priceFrom > priceTo){
            Toast.makeText(
                this,
                "Неправильный диапазон цены",
                Toast.LENGTH_SHORT
            ).show()
        } else if (sizeFrom > sizeTo) {
            Toast.makeText(
                this,
                "Неправильный диапазон размера",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            MainActivity.showJewerliesByFilters(categories, sizeFrom, sizeTo, priceFrom, priceTo)
        }
    }

    private fun setCategoryRecycler(categoryList: MutableList<String>) {
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.categoryRV.layoutManager = layoutManager

        categoryAdapter = FilterAdapter(this, categoryList)
        binding.categoryRV.adapter = categoryAdapter
    }

    private fun initSizesSpinners() {
        val brandAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, Constants.SIZES
        )
        binding.spinnerSizeFrom.adapter = brandAdapter
        binding.spinnerSizeTo.adapter = brandAdapter

        binding.spinnerSizeTo.setSelection(Constants.SIZES.lastIndex)

        binding.spinnerSizeFrom.onItemSelectedListener = object :

            AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>,

                view: View, position: Int, id: Long
            ) {


            }

            override fun onNothingSelected(parent: AdapterView<*>) {

                // написать код для выполнения какого-либо действия

            }
        }

        binding.spinnerSizeTo.onItemSelectedListener = object :

            AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>,

                view: View, position: Int, id: Long
            ) {


            }

            override fun onNothingSelected(parent: AdapterView<*>) {

                // написать код для выполнения какого-либо действия

            }
        }
    }
}