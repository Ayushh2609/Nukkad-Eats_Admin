package com.example.nukkadeatsadmin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nukkadeatsadmin.databinding.OrderDetailsBinding

class orderDetailsAdapter(
    private val context : Context,
    private val foodNames : MutableList<String>,
    private val foodImages : MutableList<String>,
    private val foodPrices : MutableList<String>,
    private val foodQuantities : MutableList<Int>
) : RecyclerView.Adapter<orderDetailsAdapter.OrderDetailsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        val binding = OrderDetailsBinding.inflate(LayoutInflater.from(parent.context) , parent , false)

        return OrderDetailsViewHolder(binding)
    }


    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = foodNames.size

    inner class OrderDetailsViewHolder(private val binding: OrderDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply{
                foodName.text = foodNames[position]
                foodPrice.text = foodPrices[position]
                foodQuantity.text = foodQuantities[position].toString()
                foodName.text = foodNames[position]
            }
        }

    }
}