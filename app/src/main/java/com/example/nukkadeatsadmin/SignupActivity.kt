package com.example.nukkadeatsadmin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nukkadeatsadmin.Modal.UserModal
import com.example.nukkadeatsadmin.databinding.ActivitySignupBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var username: String
    private lateinit var restaurantName: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager : CallbackManager

    private val binding: ActivitySignupBinding by lazy {
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

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Initializing GoogleSignIn
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        //Initialize callbackManager
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("Success", "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d("OnCancel", "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d("OnError", "facebook:onError", error)
            }
        }
        )

        //Initialize Firebase Auth
        auth = Firebase.auth

        //Initialize Firebase Database
        database = Firebase.database.reference

        binding.alreadyHaveAccount.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.createAccountButton.setOnClickListener {

            //Getting text from edit texts(User) and saving it to the variables
            username = binding.editTextUserName.text.toString().trim()
            restaurantName = binding.editTextRestroName.text.toString().trim()
            email = binding.editTextTextEmailAddress.text.toString().trim()
            password = binding.editTextTextPassword.text.toString().trim()

            if (username.isBlank()) {
                binding.editTextUserName.error = "Enter your username"
            }
            if (restaurantName.isBlank()) {
                binding.editTextRestroName.error = "Enter your Restaurant Name"
            }
            if (email.isBlank()) {
                binding.editTextTextEmailAddress.error = "Enter your Email"
            }
            if (password.isBlank()) {
                binding.editTextTextPassword.error = "Enter your Password"
            } else {
                createAccount(email, password)
            }
        }
        val statesList = arrayOf("Uttarakhand", "UttarPradesh", "Bihar")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, statesList)


        binding.autoCompleteTextView.setAdapter(adapter)

        binding.autoCompleteTextView.setOnClickListener {
            binding.autoCompleteTextView.showDropDown()
        }


        //Facebook button
        binding.facebookButton.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(
                this,
                listOf("email", "public_profile")
            )

        }

        //Google Button
        binding.googleButton.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }


    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                Log.d("Account", task.exception.toString())
            }
        }
    }

    //Save Data to Firebase Database
    private fun saveData(name : String? , resName : String? , emailId : String? , password : String? , loginMethod  : String?) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val user = UserModal(
            name = name,
            resName = resName,
            email = emailId,
            password = password,
            loginMethod = loginMethod)
        userId?.let {
            database.child("users").child(it).setValue(user)
        }
    }

    //Facebook SignIn Code
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("Facebook Tag", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Success", "signInWithCredential:success")
                    val user = auth.currentUser
                    Toast.makeText(this , "Welcome ${user?.displayName}" , Toast.LENGTH_SHORT).show()
                    saveData(user?.displayName , null , user?.email , null ,"Facebook")
                    updateUi(user)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Fail", "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Email already exists, Try with google",
                        Toast.LENGTH_LONG
                    ).show()
                    updateUi(null)
                }
            }
    }


    //Google SignIn Code
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful) {
                val account: GoogleSignInAccount = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(this, "Welcome ${user?.displayName}", Toast.LENGTH_SHORT).show()

                        //Saving data to Firebase
                        saveData(user?.displayName , null , user?.email , null ,"Google")
                        //Successfully signed in with Google
                        updateUi(authTask.result?.user)
                        finish()

                    } else {
                        //Failed to sign in with Google
                        Toast.makeText(
                            this,
                            "Failed to sign in with Google",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            } else {
                //Failed to sign in with Google
                Toast.makeText(this, "Failed to sign in with Google", Toast.LENGTH_SHORT).show()

            }
        }
    }

    //Going to next Activty
    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
    }

    //save data into database Manually
    private fun saveUserData() {
        username = binding.editTextUserName.text.toString().trim()
        restaurantName = binding.editTextRestroName.text.toString().trim()
        email = binding.editTextTextEmailAddress.text.toString().trim()
        password = binding.editTextTextPassword.text.toString().trim()

        val userData = UserModal(username, restaurantName, email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        userId?.let {
            database.child("users").child(it).setValue(userData)

        }
    }
}