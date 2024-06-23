package com.example.fetch.UI.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            binding.btnLogin.setOnClickListener {
                val i = Intent(this, LogInActivity::class.java)
                startActivity(i)
            }

            binding.btnSignUp.setOnClickListener {
                val i = Intent(this, SignUpActivity::class.java)
                startActivity(i)
            }
        }
    }
}