package com.example.nukkadeatsadmin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nukkadeats.Modal.OrderDetails
import com.example.nukkadeatsadmin.adapters.OutForDeliveryAdapter
import com.example.nukkadeatsadmin.databinding.ActivityOutForDeliveryBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding: ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }

    private lateinit var database: FirebaseDatabase
    private var listOfCompleteOrder: ArrayList<OrderDetails> = arrayListOf()

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

        //Retrieve and Display complete order
        retrieveCompleteOrderDetails()

    }

    private fun retrieveCompleteOrderDetails() {
        database = FirebaseDatabase.getInstance()
        val completeOrderReference =
            database.reference.child("CompleteOrder").orderByChild("currentTime")
        completeOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                listOfCompleteOrder.clear()

                for (orderSnapshot in snapshot.children) {
                    val completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let {
                        listOfCompleteOrder.add(it)
                    }
                }

                listOfCompleteOrder.reverse()

                setDataIntoRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    private fun setDataIntoRecyclerView() {

        val customerName = mutableListOf<String>()
        val paymentStatus = mutableListOf<Boolean>()

        for (order in listOfCompleteOrder){
            order.userName?.let {
                customerName.add(it)
            }
            paymentStatus.add(order.paymentReceived)
        }

        val adapter = OutForDeliveryAdapter( customerName , paymentStatus)
        binding.customerReceiveRecycler.layoutManager = LinearLayoutManager(this)
        binding.customerReceiveRecycler.adapter = adapter
    }
}