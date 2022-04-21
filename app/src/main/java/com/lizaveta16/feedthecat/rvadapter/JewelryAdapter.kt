package com.lizaveta16.feedthecat.rvadapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lizaveta16.feedthecat.R
import com.lizaveta16.feedthecat.databinding.JewelryListItemBinding
import com.lizaveta16.feedthecat.model.Jewelry
import com.lizaveta16.feedthecat.utils.Actions
import com.lizaveta16.feedthecat.utils.Constants
import com.lizaveta16.feedthecat.view.JewelryDetailPage
import com.squareup.picasso.Picasso


class JewelryAdapter(
    private var context: Context,
    private var jewelries: List<Jewelry>,
    private var type: Int
) : RecyclerView.Adapter<JewelryAdapter.JewelryViewHolder>() {


    class JewelryViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
        private val type = viewType

        val listItemBinding = JewelryListItemBinding.bind(itemView)
        val gridItemBinding = JewelryListItemBinding.bind(itemView)

        fun bind(res: Jewelry, ctx: Context) {
            if (type == Constants.GRID) {
                gridItemBinding.name.text = res.name
                gridItemBinding.price.text = "${res.price} руб."
                Picasso.get().load(res.image).into(gridItemBinding.jewelryImage)
            } else {
                listItemBinding.name.text = res.name
                listItemBinding.price.text = "${res.price} руб."
                Picasso.get().load(res.image).into(listItemBinding.jewelryImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JewelryViewHolder {

        val view: View = if (viewType == Constants.GRID) {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.jewelry_grid_item, parent, false)
        } else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.jewelry_list_item, parent, false)
        }
        return JewelryViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: JewelryViewHolder, position: Int) {
        holder.bind(jewelries[position], context)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, JewelryDetailPage::class.java)

            val binding = if (type == Constants.GRID) {
                holder.gridItemBinding
            } else {
                holder.listItemBinding
            }

            val options = ActivityOptions.makeSceneTransitionAnimation(
                context as Activity,
                Pair<View, String>(binding.jewelryImage, "jewelryImg")
            )

            intent.apply {
                putExtra("action", Constants.ACTIONS.getValue(Actions.ADD))
                putExtra("jewelryName", jewelries[position].name)
                putExtra("jewelryImg", jewelries[position].image)
                putExtra("jewelryPrice", jewelries[position].price)
                putExtra("jewelrySize", jewelries[position].size)
                putExtra("jewelryCategory", jewelries[position].category)
                putExtra("jewelryMaterial", jewelries[position].material)
                putExtra("jewelryForWhom", jewelries[position].forWhom)
                putExtra("jewelryInsert", jewelries[position].insert)
                putExtra("jewelrySample", jewelries[position].sample)
                putExtra("jewelryBrand", jewelries[position].brand)
                putExtra("jewelryId", jewelries[position].pid)
                putExtra("jewelryVideo", jewelries[position].video)
            }
            context.startActivity(intent, options.toBundle())
        }
    }

    override fun getItemCount(): Int {
        return jewelries.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (type == Constants.GRID) {
            Constants.GRID
        } else {
            Constants.LIST
        }
    }

    fun setType(newType: Int){
        type = newType
    }
}