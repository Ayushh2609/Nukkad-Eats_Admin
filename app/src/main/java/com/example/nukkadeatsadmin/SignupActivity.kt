package com.example.nukkadeatsadmin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nukkadeatsadmin.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var username : String
    private lateinit var restaurantName : String
    private lateinit var email : String
    private lateinit var password : String
    private lateinit var database : DatabaseReference

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
        //Initialize Firebase Auth
        auth = Firebase.auth

        //Initialize Firebase Database
        database = Firebase.database.reference



        binding.alreadyHaveAccount.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        binding.signUpCreateButton.setOnClickListener{

            //Getting text from edit texts(User) and saving it to the variables
            username = binding.editTextUserName.text.toString().trim()
            restaurantName = binding.editTextRestroName.text.toString().trim()
            email = binding.editTextTextEmailAddress.text.toString().trim()
            password = binding.editTextTextPassword.text.toString().trim()

            if(username.isBlank() || restaurantName.isBlank() || email.isBlank() || password.isBlank()){
                binding.editTextUserName.error = "Enter your username"
                binding.editTextRestroName.error = "Enter your Restaurant Name"
                binding.editTextTextEmailAddress.error = "Enter your Email"
                binding.editTextTextPassword.error = "Enter your Password"
            }else{
                createAccount(email , password)
            }
        }


        val statesList = arrayOf("Uttarakhand" , "UttarPradesh" , "Bihar")
        val adapter = ArrayAdapter(this , android.R.layout.simple_list_item_1 , statesList)


        binding.autoCompleteTextView.setAdapter(adapter)

        binding.autoCompleteTextView.setOnClickListener {
            binding.autoCompleteTextView.showDropDown()
        }


    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email , password).addOnCompleteListener { task->
            if(task.isSuccessful){
                Toast.makeText(this , "Account Created Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this , LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this , "Account Creation Failed" , Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: Failure",task.exception)
            }
        }
    }
}