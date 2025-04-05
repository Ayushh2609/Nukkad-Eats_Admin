package com.example.nukkadeatsadmin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nukkadeatsadmin.databinding.CartItemsBinding

class AllItemAdapter(
    private val foodNameList: ArrayList<String>,
    private val foodPriceList: ArrayList<String>,
    private val foodImageList: ArrayList<Int>
) : RecyclerView.Adapter<AllItemAdapter.viewHolder>() {


    private val itemQuantity = IntArray(foodNameList.size) { 1 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllItemAdapter.viewHolder {


        val binding = CartItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllItemAdapter.viewHolder, position: Int) {

        holder.bind(position)
    }

    override fun getItemCount(): Int = foodNameList.size

    inner class viewHolder(private val binding: CartItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                foodName.text = foodNameList[position]
                foodPrice.text = foodPriceList[position]
                foodImage.setImageResource(foodImageList[position])


                plusCart.setOnClickListener {
                    increaseItem(position)
                }

                minusCart.setOnClickListener {
                    decreaseItem(position)
                }

                deleteCart.setOnClickListener {
                    deleteItem(position)
                }
            }
        }


        private fun decreaseItem(position: Int) {
            if (itemQuantity[position] > 1) {
                itemQuantity[position]--
                binding.quantityCart.text = itemQuantity[position].toString()
            }
            if (itemQuantity[position] == 1) {
                deleteItem(position)
            }
        }

        private fun deleteItem(position: Int) {
            foodNameList.removeAt(position)
            foodPriceList.removeAt(position)
            foodImageList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, foodNameList.size)

        }

        private fun increaseItem(position: Int) {

            if (itemQuantity[position] < 60) {
                itemQuantity[position]++
                binding.quantityCart.text = itemQuantity[position].toString()

            }
        }

    }

}
