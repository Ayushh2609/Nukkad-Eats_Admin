package com.example.nukkadeatsadmin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nukkadeatsadmin.adapters.OutForDeliveryAdapter
import com.example.nukkadeatsadmin.databinding.ActivityOutForDeliveryBinding

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding : ActivityOutForDeliveryBinding by lazy{
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
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

        binding.backButton.setOnClickListener{
            finish()
        }

        val customerNameList = arrayListOf("Ayush Paliwal" , "Anchal" , "Abhijeet" , "Akshay kumar" , "Sunny Leone")
        val moneyStatusList = arrayListOf("Received" , "Not Received" , "Not Received" , "Received" , "Not Received")

        val adapter = OutForDeliveryAdapter(customerNameList , moneyStatusList)
        binding.customerReceiveRecycler.layoutManager = LinearLayoutManager(this)
        binding.customerReceiveRecycler.adapter = adapter

    }
}