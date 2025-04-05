package com.example.nukkadeatsadmin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nukkadeatsadmin.databinding.PendingItemsBinding

class PendingItemAdapter(private val customerList : ArrayList<String> , private val quantityList : ArrayList<String>) : RecyclerView.Adapter<PendingItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PendingItemsBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerList.size

    inner class ViewHolder(private val binding : PendingItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                customerName.text = customerList[position]
                quantityNumber.text = quantityList[position]
            }
        }

    }
}