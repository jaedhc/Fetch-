package com.example.fetch.UI.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fetch.R
import com.example.fetch.data.model.Dog

class DogsAdapter: RecyclerView.Adapter<DogsHolder>() {

    private var listOfDogs = listOf<Dog>()
    private var listener: OnDogClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogsHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.dog_item, parent, false)
        return DogsHolder(view)
    }

    override fun getItemCount(): Int = listOfDogs.size

    override fun onBindViewHolder(holder: DogsHolder, position: Int) {
        val element = listOfDogs[position]

        holder.txtName.text = element.name
        holder.txtBreed.text = element.breed
        Glide.with(holder.itemView.context).load(element.img).circleCrop().into(holder.imgDog)
    }

    fun setDogList(list: List<Dog>){
        this.listOfDogs = list
        notifyDataSetChanged()
    }

    fun onDogClickListener(listener: OnDogClickListener){
        this.listener = listener
    }

}

interface OnDogClickListener {
    fun onDogSelected(position: Int, dogs: List<Dog>)
}

class DogsHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val txtName = itemView.findViewById<TextView>(R.id.txtDogName)
    val txtBreed = itemView.findViewById<TextView>(R.id.txtBreed)
    val imgDog = itemView.findViewById<ImageView>(R.id.imageView3)
}
