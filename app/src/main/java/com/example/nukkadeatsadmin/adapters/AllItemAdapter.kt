package com.example.nukkadeatsadmin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nukkadeatsadmin.AllItemMenuActivity
import com.example.nukkadeatsadmin.databinding.CartItemsBinding

class AllItemAdapter(private val foodName : MutableList<String> , private val foodPrice : MutableList<String> , private val foodDescription : MutableList<String>) : RecyclerView.Adapter<AllItemAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllItemAdapter.viewHolder {
        val binding = CartItemsBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllItemAdapter.viewHolder, position: Int) {

        holder.bind(position)
    }

    override fun getItemCount(): Int = foodName.size

    inner class viewHolder(private val binding : CartItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {  }
        }

    }
}
