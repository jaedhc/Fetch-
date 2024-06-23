package com.example.fetch.UI.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fetch.R
import com.example.fetch.UI.adapter.DogsAdapter
import com.example.fetch.UI.viewmodel.MyDogsViewModel
import com.example.fetch.databinding.ActivityMyDogsBinding

class MyDogsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyDogsBinding
    private lateinit var dogsAdapter: DogsAdapter
    private lateinit var viewModel: MyDogsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyDogsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MyDogsViewModel::class.java)

        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        init()
    }

    private fun init() {
        dogsAdapter = DogsAdapter()

        viewModel.onCreate()

        viewModel.dogList.observe(this) {
            dogsAdapter.setDogList(it)

            binding.rvMyDogs.apply {
                layoutManager = LinearLayoutManager(this@MyDogsActivity)
                adapter = dogsAdapter
            }

        }
    }

}