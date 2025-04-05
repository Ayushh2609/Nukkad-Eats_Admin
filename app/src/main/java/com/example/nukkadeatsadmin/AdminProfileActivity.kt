package com.example.nukkadeatsadmin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nukkadeatsadmin.databinding.ActivityAdminProfileBinding

class AdminProfileActivity : AppCompatActivity() {
    private val binding : ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
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

        binding.nameEditText.isEnabled = false
        binding.addressEditText.isEnabled = false
        binding.emailEditText.isEnabled = false
        binding.phoneEditText.isEnabled = false
        binding.passwordEditText.isEnabled = false
        binding.imgRes.isEnabled = false

        var isEnable = false
        binding.editButton.setOnClickListener {
            isEnable = !isEnable

            binding.nameEditText.isEnabled = isEnable
            binding.addressEditText.isEnabled = isEnable
            binding.emailEditText.isEnabled = isEnable
            binding.phoneEditText.isEnabled = isEnable
            binding.passwordEditText.isEnabled = isEnable
            binding.imgRes.isEnabled = isEnable


            if(isEnable){
                binding.nameEditText.requestFocus()

                binding.imgRes.setOnClickListener{
                    pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            }
        }

        binding.saveInfoBtn.setOnClickListener {
            Toast.makeText(this, "Information Saved" , Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    var imageSelected = false
    val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.imgRes.setImageURI(uri)
            imageSelected = true
        }
    }
}