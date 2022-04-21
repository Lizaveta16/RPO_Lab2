package com.lizaveta16.feedthecat.rvadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lizaveta16.feedthecat.R
import com.lizaveta16.feedthecat.databinding.FilterItemBinding
import com.lizaveta16.feedthecat.model.Jewelry
import com.lizaveta16.feedthecat.utils.Categories
import com.lizaveta16.feedthecat.utils.Constants
import com.lizaveta16.feedthecat.view.MainActivity

class FilterAdapter(private var context: Context, private var categories : List<String>) : RecyclerView.Adapter<FilterAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = FilterItemBinding.bind(itemView)
        private var isSelected = false

        fun bind(category : String) = with( binding){
            categoryTitle.text = category
            itemView.setOnClickListener { view ->
                if(isSelected) {
                    binding.imageView3.setImageResource(R.drawable.ic_filter_item)
                    isSelected = !isSelected
                    categoryList.remove(categoryTitle.text.toString())
                } else {
                    binding.imageView3.setImageResource(R.drawable.ic_filter_item_selected)
                    isSelected = !isSelected
                    categoryList.add(categoryTitle.text.toString())
                }
                //MainActivity.showCoursesByCategory(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val categoryItemView : View = LayoutInflater.from(context).inflate(R.layout.filter_item, parent, false)
        return CategoryViewHolder(categoryItemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int {
        return categories.count()
    }

    companion object {
        var categoryList = mutableListOf<String>()
    }
}