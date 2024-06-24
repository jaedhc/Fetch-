package com.example.fetch.UI.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fetch.R
import com.example.fetch.data.model.Appointment
import com.example.fetch.data.model.User

class AppointmentAdapter: RecyclerView.Adapter<AppontmentHolder>() {

    private var listOfAppointment = listOf<Appointment>()
    private var listener: OnAppointmentClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppontmentHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false)
        return AppontmentHolder(view)
    }

    override fun getItemCount(): Int = listOfAppointment.size

    override fun onBindViewHolder(holder: AppontmentHolder, position: Int) {
        val element = listOfAppointment[position]

        holder.itemView.setOnClickListener {
            listener?.onUserSelected(position, listOfAppointment)
        }

        holder.txtFrom.text = element.startingLocation
        holder.txtTo.text = element.endingLocation
        holder.txtDate.text = "Date of appointment: " + element.date
    }

    fun setDogList(list: List<Appointment>){
        this.listOfAppointment = list
        notifyDataSetChanged()
    }

    fun setOnUserClickListener(listener: OnAppointmentClickListener){
        this.listener = listener
    }

    interface OnAppointmentClickListener {
        fun onUserSelected(position: Int, user: List<Appointment>)
    }

}

class AppontmentHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val txtFrom = itemView.findViewById<TextView>(R.id.txtFrom)
    val txtTo = itemView.findViewById<TextView>(R.id.txtTo)
    val txtDate = itemView.findViewById<TextView>(R.id.txtDate)

}