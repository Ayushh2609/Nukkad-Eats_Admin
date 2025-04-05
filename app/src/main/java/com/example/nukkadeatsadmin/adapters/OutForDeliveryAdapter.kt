package com.example.nukkadeatsadmin.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nukkadeatsadmin.databinding.DeliveryItemBinding

class OutForDeliveryAdapter(private val customerNameList : ArrayList<String> , private val moneyStatusList : ArrayList<String>) : RecyclerView.Adapter<OutForDeliveryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DeliveryItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }
    override fun getItemCount(): Int = customerNameList.size


    inner class ViewHolder(private val binding : DeliveryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                customerName.text = customerNameList[position]
                receiveStatus.text = moneyStatusList[position]

                val colorMap = mapOf(
                    "Received" to Color.GREEN,
                    "Not Received" to Color.RED,
                    "Pending" to Color.YELLOW
                )

                receiveStatus.setTextColor(colorMap[moneyStatusList[position]]?:Color.BLACK)
                statusCircle.backgroundTintList = ColorStateList.valueOf(colorMap[moneyStatusList[position]]?:Color.BLACK)

            }
        }

    }
}