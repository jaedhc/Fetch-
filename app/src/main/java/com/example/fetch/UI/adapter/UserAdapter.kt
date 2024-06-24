package com.example.fetch.UI.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fetch.R
import com.example.fetch.data.model.User

class UserAdapter: RecyclerView.Adapter<UserHolder>() {

    private var listOfUsers = listOf<User>()
    private var listener: OnUserClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dog_item, parent, false)
        return UserHolder(view)
    }

    override fun getItemCount(): Int = listOfUsers.size

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val element = listOfUsers[position]

        holder.itemView.setOnClickListener {
            listener?.onUserSelected(position, listOfUsers)
        }

        holder.txtName.text = element.name
        holder.txtBreed.text = element.email
        Glide.with(holder.itemView.context).load(element.imageUrl).circleCrop().into(holder.imgDog)
    }

    fun setDogList(list: List<User>){
        this.listOfUsers = list
        notifyDataSetChanged()
    }

    fun setOnUserClickListener(listener: OnUserClickListener){
        this.listener = listener
    }

    interface OnUserClickListener {
        fun onUserSelected(position: Int, user: List<User>)
    }

}

class UserHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val txtName = itemView.findViewById<TextView>(R.id.txtDogName)
    val txtBreed = itemView.findViewById<TextView>(R.id.txtBreed)
    val imgDog = itemView.findViewById<ImageView>(R.id.imageView3)
}
