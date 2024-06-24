package com.example.fetch.UI.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fetch.R
import com.example.fetch.UI.adapter.AppointmentAdapter
import com.example.fetch.UI.viewmodel.MyAppointmentsViewModel
import com.example.fetch.databinding.ActivityMyAppointmentsBinding

class MyAppointments : AppCompatActivity() {

    private lateinit var binding: ActivityMyAppointmentsBinding
    private lateinit var viewModel: MyAppointmentsViewModel

    private lateinit var appointmentAdapter: AppointmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyAppointmentsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()

    }

    fun init(){
        viewModel = ViewModelProvider(this).get(MyAppointmentsViewModel::class.java)

        viewModel.getAppointments()

        appointmentAdapter = AppointmentAdapter()

        viewModel.appointments.observe(this) { appointments ->
            appointmentAdapter.setDogList(appointments)

            binding.rvMyAppointments.apply {
                layoutManager = LinearLayoutManager(this@MyAppointments)
                adapter = appointmentAdapter
            }
        }
    }
}