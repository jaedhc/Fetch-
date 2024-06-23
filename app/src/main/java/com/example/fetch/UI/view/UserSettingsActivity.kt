package com.example.fetch.UI.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fetch.R
import com.example.fetch.databinding.ActivityUserSettingsBinding
import com.google.firebase.auth.FirebaseAuth

class UserSettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserSettingsBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserSettingsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        initView()
    }

    private fun initView(){
        auth = FirebaseAuth.getInstance()
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}