package com.example.fetch.UI.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fetch.R
import com.example.fetch.UI.viewmodel.SignUpViewModel
import com.example.fetch.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var signUpViewModel: SignUpViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signUpViewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)

        signUpViewModel.signupState.observe(this, Observer {
            if(it.equals("Created")){
                val i = Intent(this, HomeActivity::class.java)
                startActivity(i)
                finish()
            }else{
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })

        binding.btnSignUp.setOnClickListener {
            val name = binding.inputUsername.text.toString()
            val email = binding.inputEmail.text.toString()
            val pass = binding.inputPass.text.toString()

            signUpViewModel.signUp(name, email, pass)
        }

    }
}