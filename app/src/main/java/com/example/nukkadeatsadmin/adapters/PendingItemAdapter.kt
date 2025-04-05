package com.example.nukkadeatsadmin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.nukkadeatsadmin.databinding.PendingItemsBinding

class PendingItemAdapter(
    private val customerList: ArrayList<String>,
    private val quantityList: ArrayList<String>,
    private val foodImage: ArrayList<Int>
) : RecyclerView.Adapter<PendingItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            PendingItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerList.size

    inner class ViewHolder(private val binding: PendingItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isAccepted = false
        fun bind(position: Int) {
            binding.apply {
                customerName.text = customerList[position]
                quantityNumber.text = quantityList[position]
                foodItemImage.setImageResource(foodImage[position])

                acceptBtn.apply {
                    if(!isAccepted){
                        text = "Accepted"
                    }else{
                        text = "Dispatched"
                    }
                    setOnClickListener {
                        if(!isAccepted){
                            text = "Dispatched"
                            isAccepted = true
                            Toast.makeText(context , "Order Accepted" , Toast.LENGTH_SHORT).show()

                        }else{
                            customerList.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)

                            Toast.makeText(context , "Order Dispatched" , Toast.LENGTH_SHORT).show()
                        }


                    }

                }
            }
        }

    }
}