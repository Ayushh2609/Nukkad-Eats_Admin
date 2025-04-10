package com.example.nukkadeatsadmin

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isEmpty
import com.example.nukkadeatsadmin.databinding.ActivityAddMenuBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddMenuActivity : AppCompatActivity() {

    private lateinit var foodName : String
    private lateinit var foodPrice : String
    private var foodImageUri : Uri? = null
    private lateinit var foodDescription : String
    private lateinit var foodIngredients : String

    //Firebase
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase

    private val binding: ActivityAddMenuBinding by lazy {
        ActivityAddMenuBinding.inflate(layoutInflater)
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

        //Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        //Initialize Firebase Database
        database = FirebaseDatabase.getInstance()

        binding.AddItemBtn.setOnClickListener {

            foodName = binding.itemName.text.toString().trim()
            foodPrice = binding.itemPrice.text.toString().trim()
            foodDescription = binding.itemDescription.text.toString().trim()
            foodName = binding.itemIngredients.text.toString().trim()

            if(foodName.isEmpty()){
                binding.itemName.error = "Please enter the Name of the food"
            }
            if(foodPrice.isEmpty()){
                binding.itemPrice.error = "Please enter the price of the food"
            }
            if(foodDescription.isEmpty()){
                binding.itemDescription.error = "Please enter the description of the food"
            }
            if(foodIngredients.isEmpty()){
                binding.itemIngredients.error = "Please enter the Ingredients of the food"
            }
            else {
                Toast.makeText(this , "Item added successfully" , Toast.LENGTH_SHORT).show()
                uploadData()
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.addImage.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.AddItemBtn.setOnClickListener {
            var isValid = true

            if (binding.itemName.text.toString().trim().isEmpty()) {
                binding.itemName.error = "Please enter the Name of the food"
                isValid = false
            }
            if (binding.itemPrice.text.toString().trim().isEmpty()) {
                binding.itemPrice.error = "Please enter the Price of the food"
                isValid = false
            }
            if (!imageSelected) {
                binding.addImage.background = ContextCompat.getDrawable(this , R.drawable.red_border)
                isValid = false
            }else{
                binding.addImage.setBackgroundResource(0)
            }
            if (binding.itemDescription.text.toString().isEmpty()) {
                binding.itemDescription.error = "Please enter the Description of the food"
                isValid = false
            }
            if (binding.itemIngredients.text.toString().isEmpty()) {
                binding.itemIngredients.error = "Please enter the Ingredients of the food"
                isValid = false
            }
            if (isValid) {
                Toast.makeText(this, "The item has added", Toast.LENGTH_SHORT).show()
                finish()
            }

        }
    }

    private fun uploadData() {

    }

    var imageSelected = false
    val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.selectedImage.setImageURI(uri)
            imageSelected = true
        }
    }
}