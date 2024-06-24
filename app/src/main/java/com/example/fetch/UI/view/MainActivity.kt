package com.example.fetch.UI.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import com.example.fetch.R
import com.example.fetch.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            val i = Intent(this, HomeActivity::class.java)
            startActivity(i)
            finish()
        }else{
            init()
        }
    }

    private fun init(){
        binding.btnLogin.setOnClickListener {
            val i = Intent(this, LogInActivity::class.java)
            startActivity(i)
            finish()
        }

        binding.btnSignUp.setOnClickListener {
            binding.btnLogin.visibility = View.GONE
            binding.btnSignUp.visibility = View.GONE
            binding.btnWalker.visibility = View.VISIBLE
            binding.btnOwner.visibility = View.VISIBLE
        }

        binding.btnOwner.setOnClickListener {
            val i = Intent(this, SignUpActivity::class.java)
            var sharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("isWalker", false).apply()
            startActivity(i)
        }

        binding.btnWalker.setOnClickListener {
            val i = Intent(this, SignUpActivity::class.java)
            var sharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("isWalker", true).apply()
            startActivity(i)
        }
    }
}