package com.example.nukkadeatsadmin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nukkadeats.Modal.OrderDetails
import com.example.nukkadeatsadmin.adapters.PendingItemAdapter
import com.example.nukkadeatsadmin.databinding.ActivityPendingOrderBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PendingOrderActivity : AppCompatActivity() , PendingItemAdapter.OnItemClicked{
    private val binding : ActivityPendingOrderBinding by lazy{
        ActivityPendingOrderBinding.inflate(layoutInflater)
    }

    private var listOfNames : MutableList<String> = mutableListOf()
    private var listOfTotalPrice : MutableList<String> = mutableListOf()
    private var listOfImageFirstFoodOrder : MutableList<String> = mutableListOf()
    private var listOfOrderItem : ArrayList<OrderDetails> = arrayListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Initializing Database
        database = FirebaseDatabase.getInstance()

        //Initialization Of Database Reference
        databaseOrderDetails = database.reference.child("OrderDetails")

        getOrderDetails()



    }

    private fun getOrderDetails() {
        //Retrieve order details from Firebase
        databaseOrderDetails.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(orderSnapshot in snapshot.children){
                    val orderDetails = orderSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                        listOfOrderItem.add(it)
                    }
                }

                addDataToListForRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun addDataToListForRecyclerView() {
        for(orderItem in listOfOrderItem){

            orderItem.userName?.let { listOfNames.add(it) }
            orderItem.totalPrices?.let { listOfTotalPrice.add(it) }
            orderItem.foodImages?.filterNot {it.isEmpty() }?.forEach{
                listOfImageFirstFoodOrder.add(it)
            }
        }
        setAdapter()
    }

    private fun setAdapter() {
        binding.pendingRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = PendingItemAdapter(this , listOfNames , listOfTotalPrice , listOfImageFirstFoodOrder , this)
        binding.pendingRecyclerView.adapter = adapter
    }

    override fun onItemClicked(position: Int) {
        val intent = Intent(this , UserOrderDetailsActivity::class.java)
        val userOrderDetails = listOfOrderItem[position]
        intent.putExtra("userOrderDetails" ,userOrderDetails)
        startActivity(intent)
    }
}