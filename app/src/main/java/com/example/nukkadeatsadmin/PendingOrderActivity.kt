package com.example.nukkadeatsadmin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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

class PendingOrderActivity : AppCompatActivity(), PendingItemAdapter.OnItemClicked {
    private val binding: ActivityPendingOrderBinding by lazy {
        ActivityPendingOrderBinding.inflate(layoutInflater)
    }

    private var listOfNames: MutableList<String> = mutableListOf()
    private var listOfTotalPrice: MutableList<String> = mutableListOf()
    private var listOfImageFirstFoodOrder: MutableList<String> = mutableListOf()
    private var listOfOrderItem: ArrayList<OrderDetails> = arrayListOf()
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

        binding.backButton.setOnClickListener { finish()}

        //Initializing Database
        database = FirebaseDatabase.getInstance()

        //Initialization Of Database Reference
        databaseOrderDetails = database.reference.child("OrderDetails")

        getOrderDetails()


    }

    private fun getOrderDetails() {
        //Retrieve order details from Firebase
        databaseOrderDetails.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children) {
                    val orderDetails = orderSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                        listOfOrderItem.add(it)
                    }
                }

                addDataToListForRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun addDataToListForRecyclerView() {
        for (orderItem in listOfOrderItem) {

            orderItem.userName?.let { listOfNames.add(it) }
            orderItem.totalPrices?.let { listOfTotalPrice.add(it) }
            orderItem.foodImages?.filterNot { it.isEmpty() }?.forEach {
                listOfImageFirstFoodOrder.add(it)
            }
        }
        setAdapter()
    }

    private fun setAdapter() {
        binding.pendingRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter =
            PendingItemAdapter(this, listOfNames, listOfTotalPrice, listOfImageFirstFoodOrder, this)
        binding.pendingRecyclerView.adapter = adapter
    }

    override fun onItemClicked(position: Int) {
        val intent = Intent(this, UserOrderDetailsActivity::class.java)
        val userOrderDetails = listOfOrderItem[position]
        intent.putExtra("userOrderDetails", userOrderDetails)
        startActivity(intent)
    }

    override fun onItemAcceptClicked(position: Int) {
        //Handle item Acceptance and Update Database
        val childItemPushKey = listOfOrderItem[position].itemPushKey
        val clickItemOrderReference = childItemPushKey?.let {
            database.reference.child("OrderDetails").child(it)
        }
        clickItemOrderReference?.child("orderAccepted")?.setValue(true)

        updateOrderAcceptStatus(position)

    }

    private fun updateOrderAcceptStatus(position: Int) {
        //Update order acceptance in user's buy history and user details
        val userIdOfClickedItem = listOfOrderItem[position].userId
        val pushKeyOfClickedItem = listOfOrderItem[position].itemPushKey
        val buyHistoryReference =
            database.reference.child("users").child(userIdOfClickedItem!!).child("OrderHistory")
                .child(pushKeyOfClickedItem!!)

        buyHistoryReference.child("orderAccepted").setValue(true)
        databaseOrderDetails.child(pushKeyOfClickedItem).child("orderAccepted").setValue(true)

    }

    override fun onItemDispatchedClicked(position: Int) {
        val dispatchItemPushKey = listOfOrderItem[position].itemPushKey
        val dispatchItemOrderReference = database.reference.child("CompleteOrder").child(dispatchItemPushKey!!)
        dispatchItemOrderReference.setValue(listOfOrderItem[position])
            .addOnSuccessListener {
                deleteThisItemFromOrderDetails(dispatchItemPushKey)
            }
    }

    private fun deleteThisItemFromOrderDetails(dispatchItemPushKey: String){

        val orderDetailsItemReference = database.reference.child("OrderDetails").child(dispatchItemPushKey)
        orderDetailsItemReference.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this , "Order is dispatched" , Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this , "Order is not dispatched" , Toast.LENGTH_SHORT).show()

            }
    }
}