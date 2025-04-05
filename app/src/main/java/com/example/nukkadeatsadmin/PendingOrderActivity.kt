package com.example.nukkadeatsadmin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nukkadeatsadmin.adapters.PendingItemAdapter
import com.example.nukkadeatsadmin.databinding.ActivityPendingOrderBinding

class PendingOrderActivity : AppCompatActivity() {
    private val binding : ActivityPendingOrderBinding by lazy{
        ActivityPendingOrderBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val customerList = arrayListOf("Ayush" , "Anchal" , "Abhijeet" , "Munni")
        val quantityList = arrayListOf("3" , "4" , "1" , "9")
        val foodImage = arrayListOf(R.drawable.burger , R.drawable.pizza , R.drawable.burger , R.drawable.sydney)

        val adapter = PendingItemAdapter(customerList , quantityList , foodImage)
        binding.pendingRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.pendingRecyclerView.adapter = adapter
        
    }
}