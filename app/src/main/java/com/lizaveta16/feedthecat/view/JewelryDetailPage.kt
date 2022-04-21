package com.lizaveta16.feedthecat.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.MediaController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.lizaveta16.feedthecat.R
import com.lizaveta16.feedthecat.databinding.ActivityJewelryDetailPageBinding
import com.lizaveta16.feedthecat.utils.Actions
import com.lizaveta16.feedthecat.utils.Constants
import com.squareup.picasso.Picasso

@SuppressLint("SetJavaScriptEnabled")
class JewelryDetailPage : AppCompatActivity() {

    lateinit var binding: ActivityJewelryDetailPageBinding

    lateinit var pid: String
    lateinit var name: String
    lateinit var price: String
    lateinit var brand: String
    lateinit var forWhom: String
    lateinit var material: String
    lateinit var sample: String
    lateinit var insert: String
    lateinit var size: String
    lateinit var category: String
    lateinit var imageUri: String
    lateinit var videoUri: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJewelryDetailPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mediaController = MediaController(this)
        mediaController.setAnchorView(binding.jewelryVideo)

        Picasso.get().load(intent.getStringExtra("jewelryImg")).into(binding.jewelryImg)

        pid = intent.getStringExtra("jewelryId").toString()
        imageUri = intent.getStringExtra("jewelryImg").toString()
        name = intent.getStringExtra("jewelryName").toString()
        price = intent.getDoubleExtra("jewelryPrice", 0.0).toString()
        brand = intent.getStringExtra("jewelryBrand").toString()
        forWhom = intent.getStringExtra("jewelryForWhom").toString()
        material = intent.getStringExtra("jewelryMaterial").toString()
        sample = intent.getStringExtra("jewelrySample").toString()
        insert = intent.getStringExtra("jewelryInsert").toString()
        size = intent.getDoubleExtra("jewelrySize", 0.0).toString()
        category = intent.getStringExtra("jewelryCategory").toString()
        videoUri = intent.getStringExtra("jewelryVideo").toString()

        binding.jewelryName.text = name
        binding.jewelryPrice.text = "$price руб."
        binding.jewelryBrand.text = brand
        binding.jewelryForWhom.text = forWhom
        binding.jewelryMaterial.text = material
        binding.jewelrySample.text = sample
        binding.jewelryInsert.text = insert
        binding.jewelrySize.text = size
        binding.jewelryVideo.setMediaController(mediaController)
        binding.jewelryVideo.setVideoURI(Uri.parse(intent.getStringExtra("jewelryVideo")))
        binding.jewelryVideo.start()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_page_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                val intent = Intent(this@JewelryDetailPage, AddEditJewelryActivity::class.java)

                intent.apply {
                    putExtra("action", Constants.ACTIONS.getValue(Actions.EDIT))
                    putExtra("jewelryName", name)
                    putExtra("jewelryImg", imageUri)
                    putExtra("jewelryPrice", price.toDouble())
                    putExtra("jewelrySize", size.toDouble())
                    putExtra("jewelryCategory", category)
                    putExtra("jewelryMaterial", material)
                    putExtra("jewelryForWhom", forWhom)
                    putExtra("jewelryInsert", insert)
                    putExtra("jewelrySample", sample)
                    putExtra("jewelryBrand", brand)
                    putExtra("jewelryId", pid)
                    putExtra("jewelryVideo", videoUri)
                }

                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}