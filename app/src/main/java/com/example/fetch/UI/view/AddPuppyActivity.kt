package com.example.fetch.UI.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.fetch.UI.viewmodel.AddPuppyViewModel
import com.example.fetch.databinding.ActivityAddPuppyBinding
import kotlinx.coroutines.runBlocking

class AddPuppyActivity : AppCompatActivity() {
    private lateinit var addPuppyViewModel: AddPuppyViewModel
    private lateinit var binding: ActivityAddPuppyBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var getImage: ActivityResultLauncher<String>
    private var granted: Boolean = false
    var image: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityAddPuppyBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.entries.all { it.value }){
                granted = true
            }
        }

        getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            image = uri
            binding.imgPuppy.setImageURI(uri)
        }

        init()
    }

    fun init() {

        binding.contraintImgPuppy.setOnClickListener {
            if(!granted){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(arrayOf(
                        android.Manifest.permission.READ_MEDIA_IMAGES,
                        android.Manifest.permission.READ_MEDIA_VIDEO
                    ))
                } else {
                    requestPermissionLauncher.launch(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE))
                }
            } else {
                getImage.launch("image/*")
            }
        }

        addPuppyViewModel = ViewModelProvider(this).get(AddPuppyViewModel::class.java)

        addPuppyViewModel.onCreate()

        addPuppyViewModel.breedModel.observe(this) { fort ->
            val breedsName = mutableListOf<String>()
            fort.forEach {
                breedsName.add(it.name)
            }
            val adapter = ArrayAdapter(this, android.R.layout.select_dialog_item, breedsName)
            binding.autoCompleteTextView.threshold = 1
            binding.autoCompleteTextView.setAdapter(adapter)
        }

        addPuppyViewModel.isLoading.observe(this) {
            if(it){
                binding.progressBar.visibility = View.VISIBLE
                binding.contraintMain.visibility = View.INVISIBLE
            } else {
                binding.progressBar.visibility = View.INVISIBLE
                binding.contraintMain.visibility = View.VISIBLE
            }
        }

        addPuppyViewModel.created.observe(this) {
            if(it.equals("Created")){
                val i = Intent(this, HomeActivity::class.java)
                Toast.makeText(this, "Dog Saved", Toast.LENGTH_LONG).show()
                startActivity(i)
                finish()
            } else {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        binding.btnSubmit.setOnClickListener {
            verifyData()
        }

    }

    private fun verifyData() {
        val breed = binding.autoCompleteTextView.text.toString()
        val name = binding.inputName.text.toString()
        val age = binding.inputAge.text.toString()
        val description = binding.inputDescription.text.toString()

        var dogImage = "https://firebasestorage.googleapis.com/v0/b/fetch-773cc.appspot.com/o/puppy.jpg?alt=media&token=58697f26-1e7c-44a9-bc6c-8a56bef3677c"
        if(image != null){
            dogImage = runBlocking { addPuppyViewModel.uploadImg(breed, image!!) }
        }

        if(name.isNotEmpty() && age.isNotEmpty() && description.isNotEmpty()){
            addPuppyViewModel.addPuppy(dogImage, breed, name, age, description)
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show()
        }

    }

}