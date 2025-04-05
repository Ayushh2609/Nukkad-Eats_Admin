package com.example.nukkadeatsadmin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nukkadeatsadmin.adapters.AllItemAdapter
import com.example.nukkadeatsadmin.databinding.ActivityAllItemMenuBinding

class AllItemMenuActivity : AppCompatActivity() {
    private val binding : ActivityAllItemMenuBinding by lazy {
        ActivityAllItemMenuBinding.inflate(layoutInflater)
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

        binding.backButton.setOnClickListener {
            finish()
        }

        val foodNameList = arrayListOf("Burger" , "Spring roll" , "Parathe" , "Sydney Sweeney kachi Ghani")
        val foodPriceList = arrayListOf("50" , "40" , "30" , "6969")
        val foodImageList = arrayListOf(R.drawable.burger , R.drawable.pizza , R.drawable.burger , R.drawable.sydney)

        val adapter = AllItemAdapter(foodNameList , foodPriceList , foodImageList)

        binding.recyclerItems.layoutManager = LinearLayoutManager(this)
        binding.recyclerItems.adapter = adapter
    }
}