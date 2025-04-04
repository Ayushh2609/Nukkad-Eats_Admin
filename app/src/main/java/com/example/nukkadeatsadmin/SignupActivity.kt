package com.example.nukkadeatsadmin

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nukkadeatsadmin.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private val binding : ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
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

        binding.alreadyHaveAccount.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        binding.signUpCreateButton.setOnClickListener{
            val intent = Intent(this , MainActivity::class.java)
            startActivity(intent)
        }


        val statesList = arrayOf("Uttarakhand" , "UttarPradesh" , "Bihar")
        val adapter = ArrayAdapter(this , android.R.layout.simple_list_item_1 , statesList)


        binding.autoCompleteTextView.setAdapter(adapter)

        binding.autoCompleteTextView.setOnClickListener {
            binding.autoCompleteTextView.showDropDown()
        }


    }
}