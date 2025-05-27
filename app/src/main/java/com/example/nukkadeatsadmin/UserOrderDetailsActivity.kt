package com.example.nukkadeatsadmin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nukkadeats.Modal.OrderDetails
import com.example.nukkadeatsadmin.databinding.ActivityUserOrderDetailsBinding

class UserOrderDetailsActivity : AppCompatActivity() {

    private val binding : ActivityUserOrderDetailsBinding by lazy {
        ActivityUserOrderDetailsBinding.inflate(layoutInflater)
    }

    private var userName : String? = null
    private var address : String? = null
    private var phoneNumber : String? = null
    private var totalPrice : String? = null

    private var foodNames : MutableList<String> = mutableListOf()
    private var foodImages : MutableList<String> = mutableListOf()
    private var foodPrices : MutableList<String> = mutableListOf()
    private var foodQuantities : MutableList<Int> = mutableListOf()


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

        getDataFromIntent()


    }

    private fun getDataFromIntent() {
        val receivedOrderDetails = intent.getParcelableExtra<OrderDetails>("userOrderDetails")
        if(receivedOrderDetails != null){
            userName = receivedOrderDetails.userName
            foodNames = receivedOrderDetails.foodNames!!
            foodImages = receivedOrderDetails.foodImages!!
            foodPrices = receivedOrderDetails.foodPrices!!
            foodQuantities = receivedOrderDetails.foodQuantities!!
            address = receivedOrderDetails.address
            phoneNumber = receivedOrderDetails.phoneNumber
            totalPrice = receivedOrderDetails.totalPrices

            setUserDetails()

            setAdapter()
        }

    }


    private fun setUserDetails() {
        binding.customerName.text = userName
        binding.customerAddress.text = address
        binding.customerPhone.text = phoneNumber
        binding.totalPayAmount.text = totalPrice
    }

    private fun setAdapter() {
        binding.orderDetailsRecycler.layoutManager = LinearLayoutManager(this)

    }
}