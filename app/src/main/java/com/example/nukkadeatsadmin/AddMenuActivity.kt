package com.example.nukkadeatsadmin

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nukkadeatsadmin.Modal.AllMenu
import com.example.nukkadeatsadmin.databinding.ActivityAddMenuBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddMenuActivity : AppCompatActivity() {

    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private var foodImageUri: Uri? = null
    private lateinit var foodDescription: String
    private lateinit var foodIngredients: String

    //Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

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
            foodIngredients = binding.itemIngredients.text.toString().trim()

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
                binding.addImage.background = ContextCompat.getDrawable(this, R.drawable.red_border)
                isValid = false
            } else {
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
                uploadData()
                finish()
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.addImage.setOnClickListener {
            pickImage.launch("image/*")
        }
    }

    private fun uploadData() {
        //Get a reference to the "menu" node in the Database
        val menuRef = database.getReference("menu")

        // Generate a unique key for the menu Item
        val newItemKey = menuRef.push().key


        if (foodImageUri != null) {
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("menu_images/${newItemKey}.jpg")

            val uploadTask = imageRef.putFile(foodImageUri!!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->

                    //Create a new Menu Item
                    val newItem = AllMenu(
                        foodName = foodName,
                        foodPrice = foodPrice,
                        foodDescription = foodDescription,
                        foodIngredient = foodIngredients,
                        foodImage = downloadUrl.toString()
                    )
                    newItemKey?.let { key ->
                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(this, "Data Upload Successfully", Toast.LENGTH_SHORT)
                                .show()
                        }
                            .addOnFailureListener {
                                Toast.makeText(this, "Data Upload Failed", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                }

            }
                .addOnFailureListener {
                    Toast.makeText(this, "Image Upload Failed", Toast.LENGTH_SHORT).show()

                }

        } else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()

        }

    }

    var imageSelected = false
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.selectedImage.setImageURI(uri)
            foodImageUri = uri
            imageSelected = true
        }
    }
}