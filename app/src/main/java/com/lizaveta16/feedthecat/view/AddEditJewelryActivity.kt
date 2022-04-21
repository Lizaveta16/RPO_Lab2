package com.lizaveta16.feedthecat.view

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.lizaveta16.feedthecat.databinding.ActivityAddEditJewelryBinding
import com.lizaveta16.feedthecat.model.Jewelry
import com.lizaveta16.feedthecat.utils.Actions
import com.lizaveta16.feedthecat.utils.Constants
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


class AddEditJewelryActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddEditJewelryBinding
    lateinit var name: String
    lateinit var price: String
    lateinit var brand: String
    lateinit var forWhom: String
    lateinit var material: String
    lateinit var sample: String
    lateinit var insert: String
    lateinit var size: String
    lateinit var category: String
    var imageUri: Uri? = null
    var videoUri: Uri? = null
    lateinit var jewelryImageRef: StorageReference
    lateinit var jewelryVideoRef: StorageReference
    lateinit var jewelryRandomKey: String
    lateinit var downloadImageUrl: String
    lateinit var downloadVideoUrl: String
    lateinit var jewelriesDBRef: DatabaseReference
    lateinit var loadingBar: ProgressDialog
    var action: Actions = Actions.ADD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditJewelryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingBar = ProgressDialog(this)

        jewelryImageRef = FirebaseStorage.getInstance().reference.child("Jewelry images")
        jewelryVideoRef = FirebaseStorage.getInstance().reference.child("Jewelry videos")

        jewelriesDBRef = FirebaseDatabase.getInstance().reference.child("Jewelries")

        binding.uploadPhoto.setOnClickListener {
            ImagePicker.with(this)
                .start()
        }

        binding.uploadVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "video/*"
            startActivityForResult(intent, 45)
        }

        binding.addButton.setOnClickListener {
            validateProductData()
        }
    }

    override fun onStart() {
        super.onStart()

        action = if(Constants.ACTIONS.getValue(Actions.EDIT) == intent.getStringExtra("action")){
            Actions.EDIT
        } else {
            Actions.ADD
        }

        initCategorySpinner()
        initBrandSpinner()
        initForWhomSpinner()
        initInsertSpinner()
        initMetalSpinner()
        initSampleSpinner()
        initSizeSpinner()

        if(action == Actions.EDIT){
            binding.nameET.setText(intent.getStringExtra("jewelryName"))
            binding.priceET.setText(intent.getDoubleExtra("jewelryPrice", 0.0).toString())
            Picasso.get().load(intent.getStringExtra("jewelryImg")).into(binding.image)

            val mediaController = MediaController(this)
            mediaController.setAnchorView(binding.jewelryVideo)
            binding.jewelryVideo.setMediaController(mediaController)
            binding.jewelryVideo.setVideoURI(Uri.parse(intent.getStringExtra("jewelryVideo")))
            binding.jewelryVideo.start()

            downloadImageUrl = intent.getStringExtra("jewelryImg").toString()
            name = intent.getStringExtra("jewelryName").toString()
            price = intent.getDoubleExtra("jewelryPrice", 0.0).toString()
            brand = intent.getStringExtra("jewelryBrand").toString()
            forWhom = intent.getStringExtra("jewelryForWhom").toString()
            material = intent.getStringExtra("jewelryMaterial").toString()
            sample = intent.getStringExtra("jewelrySample").toString()
            insert = intent.getStringExtra("jewelryInsert").toString()
            size = intent.getDoubleExtra("jewelrySize", 0.0).toString()
            category = intent.getStringExtra("jewelryCategory").toString()
            downloadVideoUrl = intent.getStringExtra("jewelryVideo").toString()

            binding.addButton.text = "Применить"
        } else {
            binding.addButton.text = "Добавить"
        }
    }

    private fun validateProductData() {
        name = binding.nameET.text.toString()
        price = binding.priceET.text.toString()
        brand = binding.spinnerBrand.selectedItem.toString()
        forWhom = binding.spinnerForWhom.selectedItem.toString()
        material = binding.spinnerMetal.selectedItem.toString()
        sample = binding.spinnerSample.selectedItem.toString()
        insert = binding.spinnerInsert.selectedItem.toString()
        size = binding.spinnerSize.selectedItem.toString()
        category = binding.spinnerCategory.selectedItem.toString()

        if (imageUri == null && action != Actions.EDIT) {
            Toast.makeText(this, "Добавьте изображение", Toast.LENGTH_SHORT).show()
        } else if (videoUri == null && action != Actions.EDIT) {
            Toast.makeText(this, "Добавьте видео", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Введите наименование товара", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(price)) {
            Toast.makeText(this, "Введите цену товара", Toast.LENGTH_SHORT).show()
        } else {
            storeProductInfo()
        }
    }

    private fun storeProductInfo() {
        loadingBar.setTitle("Загрузка данных");
        loadingBar.setMessage("Пожалуйста, подождите...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        if(action == Actions.EDIT) {
            jewelryRandomKey = intent.getStringExtra("jewelryId").toString()
        } else {
            val calendar = Calendar.getInstance()

            val currentDateFormat = SimpleDateFormat("dd.MM.yyyy")
            var currentDate = currentDateFormat.format(calendar.time)

            val currentTimeFormat = SimpleDateFormat("HH.mm.ss")
            var currentTime = currentTimeFormat.format(calendar.time)

            jewelryRandomKey = currentDate + currentTime
        }

        if(imageUri != null){
            val imageFilePath: StorageReference =
                jewelryImageRef.child(imageUri?.lastPathSegment + jewelryRandomKey)
            val uploadImageTask: UploadTask = imageFilePath.putFile(imageUri!!)
            uploadImageTask.addOnFailureListener {
                val message = it.toString()
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener {
                Toast.makeText(this, "Изображение успешно загружено", Toast.LENGTH_SHORT).show()

                uploadImageTask.continueWithTask(Continuation<UploadTask.TaskSnapshot?, Task<Uri?>?> { task ->
                    if (!task.isSuccessful) {
                        throw task.exception!!
                    }
                    downloadImageUrl = imageFilePath.downloadUrl.toString()
                    imageFilePath.downloadUrl
                }).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        downloadImageUrl = task.result.toString()
                        Toast.makeText(
                            this,
                            "Фото сохранено",
                            Toast.LENGTH_SHORT
                        ).show()
                        if(action == Actions.EDIT){
                            saveProductInfoToDatabase()
                        }
                    }
                }
            }
        }

        if(videoUri != null) {
            val videoFilePath: StorageReference =
                jewelryImageRef.child(videoUri?.lastPathSegment + jewelryRandomKey)

            val uploadVideoTask: UploadTask = videoFilePath.putFile(videoUri!!)

            uploadVideoTask.addOnFailureListener {
                val message = it.toString()
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener {
                Toast.makeText(this, "Видео успешно загружено", Toast.LENGTH_SHORT).show()

                uploadVideoTask.continueWithTask(Continuation<UploadTask.TaskSnapshot?, Task<Uri?>?> { task ->
                    if (!task.isSuccessful) {
                        throw task.exception!!
                    }
                    downloadVideoUrl = videoFilePath.downloadUrl.toString()
                    videoFilePath.downloadUrl
                }).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        downloadVideoUrl = task.result.toString()
                        Toast.makeText(
                            this,
                            "Видео сохранено",
                            Toast.LENGTH_SHORT
                        ).show()
                        if(imageUri == null || action == Actions.ADD) {
                            saveProductInfoToDatabase()
                        }
                    }
                }
            }
        } else {
            saveProductInfoToDatabase()
        }
    }

    private fun saveProductInfoToDatabase() {
        val newJewelry = Jewelry(
            jewelryRandomKey,
            name,
            downloadImageUrl,
            price.toDouble(),
            size.toDouble(),
            category,
            material,
            forWhom,
            insert,
            sample,
            brand,
            downloadVideoUrl
        )

        if(action == Actions.EDIT){
            jewelriesDBRef.child(jewelryRandomKey.replace(".", "")).setValue(newJewelry.toMap()).addOnCompleteListener {
                if(it.isSuccessful){
                    loadingBar.dismiss()
                    Toast.makeText(
                        this,
                        "Товар обновлен",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(
                        this,
                        MainActivity::class.java
                    )
                    startActivity(intent)
                } else {
                    val message: String = it.getException().toString()
                    Toast.makeText(
                        this,
                        "Ошибка: $message",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadingBar.dismiss()
                }
            }
        } else {
            jewelriesDBRef.child(jewelryRandomKey.replace(".", "")).updateChildren(newJewelry.toMap()).addOnCompleteListener{
                if(it.isSuccessful){
                    loadingBar.dismiss()
                    Toast.makeText(
                        this,
                        "Товар добавлен",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(
                        this,
                        MainActivity::class.java
                    )
                    startActivity(intent)
                } else {
                    val message: String = it.getException().toString()
                    Toast.makeText(
                        this,
                        "Ошибка: $message",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadingBar.dismiss()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 45 && resultCode == Activity.RESULT_OK) {
            videoUri = data?.data!!
            binding.jewelryVideo.setVideoURI(videoUri)
            binding.jewelryVideo.start()
        } else {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    imageUri = data?.data!!
                    binding.image.setImageURI(imageUri)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initCategorySpinner() {
        val brandAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, Constants.CATEGORIES.values.toMutableList()
        )
        binding.spinnerCategory.adapter = brandAdapter

        if(action == Actions.EDIT){
            binding.spinnerCategory.setSelection(Constants.CATEGORIES.values.indexOf(intent.getStringExtra("jewelryCategory")))
        }

        binding.spinnerCategory.onItemSelectedListener = object :

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

    private fun initBrandSpinner() {
        val brandAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, Constants.BRANDS
        )
        binding.spinnerBrand.adapter = brandAdapter

        if(action == Actions.EDIT){
            binding.spinnerBrand.setSelection(Constants.BRANDS.indexOf(intent.getStringExtra("jewelryBrand")))
        }

        binding.spinnerBrand.onItemSelectedListener = object :

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

    private fun initForWhomSpinner() {
        val brandAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, Constants.FOR_WHOM.values.toMutableList()
        )
        binding.spinnerForWhom.adapter = brandAdapter

        if(action == Actions.EDIT){
            binding.spinnerForWhom.setSelection(Constants.FOR_WHOM.values.indexOf(intent.getStringExtra("jewelryForWhom")))
        }

        binding.spinnerForWhom.onItemSelectedListener = object :

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

    private fun initMetalSpinner() {
        val brandAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, Constants.MATERIALS.values.toMutableList()
        )
        binding.spinnerMetal.adapter = brandAdapter

        if(action == Actions.EDIT){
            binding.spinnerMetal.setSelection(Constants.MATERIALS.values.indexOf(intent.getStringExtra("jewelryMaterial")))
        }

        binding.spinnerMetal.onItemSelectedListener = object :

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

    private fun initSampleSpinner() {
        val brandAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, Constants.SAMPLES
        )
        binding.spinnerSample.adapter = brandAdapter

        if(action == Actions.EDIT){
            binding.spinnerSample.setSelection(Constants.SAMPLES.indexOf(intent.getStringExtra("jewelrySample")?.toInt()))
        }

        binding.spinnerSample.onItemSelectedListener = object :

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

    private fun initInsertSpinner() {
        val brandAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, Constants.INSERT.values.toMutableList()
        )
        binding.spinnerInsert.adapter = brandAdapter

        if(action == Actions.EDIT){
            binding.spinnerInsert.setSelection(Constants.INSERT.values.indexOf(intent.getStringExtra("jewelryInsert")))
        }

        binding.spinnerInsert.onItemSelectedListener = object :

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

    private fun initSizeSpinner() {
        val brandAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, Constants.SIZES
        )
        binding.spinnerSize.adapter = brandAdapter

        if(action == Actions.EDIT){
            binding.spinnerSize.setSelection(Constants.SIZES.indexOf(intent.getStringExtra("jewelrySize")?.toDouble()))
        }

        binding.spinnerSize.onItemSelectedListener = object :

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