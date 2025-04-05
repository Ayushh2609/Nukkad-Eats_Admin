package com.example.nukkadeatsadmin

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

class AddMenuActivity : AppCompatActivity() {
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
                binding.itemDescription.error = "Please enter the Price of the food"
                isValid = false
            }
            if (binding.itemIngredients.text.toString().isEmpty()) {
                binding.itemIngredients.error = "Please enter the Price of the food"
                isValid = false
            }
            if (isValid) {
                Toast.makeText(this, "The item has added", Toast.LENGTH_SHORT).show()
                finish()
            }

        }
    }

    var imageSelected = false
    val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.selectedImage.setImageURI(uri)
            imageSelected = true
        }
    }
}