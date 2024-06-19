package com.example.fetch.UI.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fetch.R
import com.example.fetch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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