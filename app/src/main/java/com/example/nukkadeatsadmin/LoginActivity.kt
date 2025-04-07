package com.example.nukkadeatsadmin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nukkadeatsadmin.Modal.UserModal
import com.example.nukkadeatsadmin.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var passw: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient : GoogleSignInClient

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
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
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Initializing GoogleSignIn
        googleSignInClient = GoogleSignIn.getClient(this , googleSignInOptions)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Initialize Database
        database = Firebase.database.reference

        binding.loginButton.setOnClickListener {
            binding.apply {
                email = emailID.text.toString().trim()
                passw = password.text.toString().trim()

                if (email.isBlank()) {
                    emailID.error = "Enter the Email"
                }
                if (passw.isBlank()) {
                    password.error = "Enter the Password"
                } else {
                    createUserAccout(email, passw)
                }
            }
        }

        binding.dontHaveAccount.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createUserAccout(email: String, passw: String) {
        auth.signInWithEmailAndPassword(email, passw).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                updateUi(user)
            } else {
                Toast.makeText(this, "User Not Found", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this , SignupActivity::class.java))
//                auth.createUserWithEmailAndPassword(email, passw).addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        val user = auth.currentUser
//                        saveData()
//                        updateUi(user)
//                        Toast.makeText(this, "New Account Created Successfully", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show()
//                    }
//                }
            }
        }
    }

    private fun saveData() {
        email = binding.emailID.text.toString().trim()
        passw = binding.password.text.toString().trim()

        val user = UserModal(email, passw)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        userId?.let {
            database.child("users").child(it).setValue(user)
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
    }
}