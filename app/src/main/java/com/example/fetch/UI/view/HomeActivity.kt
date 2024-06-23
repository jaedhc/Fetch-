package com.example.fetch.UI.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.bumptech.glide.Glide.init
import com.example.fetch.R
import com.example.fetch.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
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
        binding.btnSettings.setOnClickListener {
            val i = Intent(this, UserSettingsActivity::class.java)
            startActivity(i)
        }

        binding.cardViewPet.setOnClickListener {
            val i = Intent(this, AddPuppyActivity::class.java)
            startActivity(i)
        }
    }
}