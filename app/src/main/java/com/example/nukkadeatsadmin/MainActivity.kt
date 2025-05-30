package com.example.nukkadeatsadmin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nukkadeats.Modal.OrderDetails
import com.example.nukkadeatsadmin.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var completeOrderReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.addMenu.setOnClickListener {
            startActivity(Intent(this, AddMenuActivity::class.java))

        }

        binding.addAllItem.setOnClickListener {
            startActivity(Intent(this, AllItemMenuActivity::class.java))
        }

        binding.outForDelivery.setOnClickListener {
            startActivity(Intent(this, OutForDeliveryActivity::class.java))
        }

        binding.feedback.setOnClickListener {
            startActivity(Intent(this, FeedbackActivity::class.java))
        }

        binding.profile.setOnClickListener {
            startActivity(Intent(this, AdminProfileActivity::class.java))
        }

        binding.moneyOnHold.setOnClickListener {

        }

        binding.createNewUser.setOnClickListener {
            startActivity(Intent(this, CreateNewUserActivity::class.java))
        }

        binding.logout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this , SignupActivity::class.java))
            finish()
        }


        binding.pendingOrders.setOnClickListener {
            startActivity(Intent(this, PendingOrderActivity::class.java))
        }


        pendingOrders()

        completeOrder()

        wholeTimeEarning()

    }

    private fun wholeTimeEarning() {
        completeOrderReference = database.reference.child("CompleteOrder")
        completeOrderReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val listOfTotalEarning = mutableListOf<Int>()

                if (!snapshot.exists()) {
                    binding.totalEarning.text = "0"
                    return
                }

                for (orderSnapshot in snapshot.children) {
                    val completeOrder = orderSnapshot.getValue(OrderDetails::class.java)

                    completeOrder?.totalPrices?.toIntOrNull()
                        ?.let { i ->
                            listOfTotalEarning.add(i)

                        }
                }
                binding.totalEarning.text = listOfTotalEarning.sum().toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun completeOrder() {
        val completeOrderReference = database.reference.child("CompleteOrder")

        var completeOrderCount = 0
        completeOrderReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (!snapshot.exists()) {
                    binding.completeOrder.text = "0"
                    return
                }

                completeOrderCount = snapshot.childrenCount.toInt()
                binding.completeOrder.text = completeOrderCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun pendingOrders() {
        database = FirebaseDatabase.getInstance()
        val pendingOrderReference = database.reference.child("OrderDetails")

        var pendingOrderCount = 0
        pendingOrderReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (!snapshot.exists()) {
                    binding.pendingOrderCount.text = "0"
                    return
                }

                pendingOrderCount = snapshot.childrenCount.toInt()
                binding.pendingOrderCount.text = pendingOrderCount.toString()
            }


            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}