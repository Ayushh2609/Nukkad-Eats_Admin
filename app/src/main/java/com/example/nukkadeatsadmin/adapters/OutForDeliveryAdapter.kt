package com.example.nukkadeatsadmin.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nukkadeatsadmin.databinding.DeliveryItemBinding

class OutForDeliveryAdapter(private val customerNameList : MutableList<String> , private val moneyStatusList : MutableList<Boolean>) : RecyclerView.Adapter<OutForDeliveryAdapter.ViewHolder>() {

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

                if(moneyStatusList[position] == true){
                    receiveStatus.text = "Received"

                }else{
                    receiveStatus.text = "Not Received"
                }

                val colorMap = mapOf(
                     true to Color.GREEN,
                    false to Color.RED
                )

                receiveStatus.setTextColor(colorMap[moneyStatusList[position]]?:Color.BLACK)
                statusCircle.backgroundTintList = ColorStateList.valueOf(colorMap[moneyStatusList[position]]?:Color.BLACK)

            }
        }

    }
}