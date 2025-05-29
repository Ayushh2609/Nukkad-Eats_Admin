package com.example.nukkadeatsadmin.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nukkadeatsadmin.R
import com.example.nukkadeatsadmin.databinding.PendingItemsBinding

class PendingItemAdapter(
    private val context: Context,
    private val customerNameList: MutableList<String>,
    private val quantityList: MutableList<String>,
    private val foodImage: MutableList<String>,
    private val itemClicked : OnItemClicked

) : RecyclerView.Adapter<PendingItemAdapter.ViewHolder>() {

    interface OnItemClicked{
        fun onItemClicked(position: Int)
        fun onItemAcceptClicked(position: Int)
        fun onItemDispatchedClicked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            PendingItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerNameList.size

    inner class ViewHolder(private val binding: PendingItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isAccepted = false
        fun bind(position: Int) {
            binding.apply {
                customerName.text = customerNameList[position]
                quantityNumber.text = quantityList[position]

                val uriString = foodImage[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(foodItemImage)

                acceptBtn.apply {
                    if (!isAccepted) {
                        text = "Accepted"
                    } else {
                        text = "Dispatched"
                    }
                    setOnClickListener {
                        if (!isAccepted) {
                            background = ContextCompat.getDrawable(context, R.drawable.green_button)
                            text = "Dispatched"
                            isAccepted = true
                            Toast.makeText(context, "Order Accepted", Toast.LENGTH_SHORT).show()

                            itemClicked.onItemAcceptClicked(position)

                        } else {
                            customerNameList.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)

                            Toast.makeText(context, "Order Dispatched", Toast.LENGTH_SHORT).show()

                            itemClicked.onItemDispatchedClicked(position)
                        }


                    }

                }

                itemView.setOnClickListener {
                    itemClicked.onItemClicked(position)
                }
            }
        }

    }
}