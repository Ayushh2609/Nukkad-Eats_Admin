package com.example.nukkadeatsadmin.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nukkadeatsadmin.Modal.AllMenu
import com.example.nukkadeatsadmin.R
import com.example.nukkadeatsadmin.databinding.CartItemsBinding
import com.google.firebase.database.DatabaseReference

class AllItemAdapter(
    private val context: Context,
    private val menuList: ArrayList<AllMenu>,
    databaseReference: DatabaseReference

) : RecyclerView.Adapter<AllItemAdapter.viewHolder>() {


    private val itemQuantity = IntArray(menuList.size) { 1 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllItemAdapter.viewHolder {
        val binding = CartItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllItemAdapter.viewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuList.size

    inner class viewHolder(private val binding: CartItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantity[position]
                val menuItem = menuList[position]
                val uriString = menuItem.foodImage
                val uri = Uri.parse(uriString)

                foodName.text = menuItem.foodName
                foodPrice.text = menuItem.foodPrice

                Glide.with(context).load(uri).into(foodImage)


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
            menuList.removeAt(position)
            menuList.removeAt(position)
            menuList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, menuList.size)

        }

        private fun increaseItem(position: Int) {

            if (itemQuantity[position] < 60) {
                itemQuantity[position]++
                binding.quantityCart.text = itemQuantity[position].toString()

            }
        }

    }

}
