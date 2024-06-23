package com.example.fetch.UI.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.example.fetch.R
import com.example.fetch.UI.viewmodel.HomeViewModel
import com.example.fetch.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        if(auth.currentUser != null){
            initView()
        } else {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun initView(){
        binding.imgUser.setOnClickListener {
            val i = Intent(this, UserSettingsActivity::class.java)
            startActivity(i)
        }

        binding.cardViewPet.setOnClickListener {
            val i = Intent(this, AddPuppyActivity::class.java)
            startActivity(i)
        }

        binding.cardViewMyDogs.setOnClickListener {
            val i = Intent(this, MyDogsActivity::class.java)
            startActivity(i)
        }

        binding.cardViewWalker.setOnClickListener {
            val i = Intent(this, ConnectWithWalkerActivity::class.java)
            startActivity(i)
        }

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        homeViewModel.user.observe(this) {
            var sharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
            var editor = sharedPreferences.edit()
            editor.putString("username", it)
            editor.apply()
            binding.txtUsername.text = "Welcome Back $it"
        }

        homeViewModel.userImg.observe(this) {
            Glide.with(this).load(it).circleCrop().into(binding.imgUser)
            var sharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
            sharedPreferences.edit().putString("userImg", it).apply()
        }

        homeViewModel.isLoading.observe(this) {
            if(it){
                binding.progressBar.visibility = View.VISIBLE
                binding.contraintMain.visibility = View.INVISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.contraintMain.visibility = View.VISIBLE
            }
        }

        homeViewModel.getUserData()
    }
}