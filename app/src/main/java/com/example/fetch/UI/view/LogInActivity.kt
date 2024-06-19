package com.example.fetch.UI.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fetch.R
import com.example.fetch.UI.viewmodel.LogInViewModel
import com.example.fetch.UI.viewmodel.SignUpViewModel
import com.example.fetch.databinding.ActivityLogInBinding

class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    private lateinit var logInViewModel: LogInViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logInViewModel = ViewModelProvider(this).get(LogInViewModel::class.java)

        logInViewModel.loginState.observe(this, Observer {
            if(it.equals("Logged")){
                val i = Intent(this, HomeActivity::class.java)
                startActivity(i)
            } else {
                Log.d("Error", it)
            }
        })

        binding.btnLogIn.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val pass = binding.inputPass.text.toString()
            logInViewModel.logIn(email, pass)
        }
    }
}