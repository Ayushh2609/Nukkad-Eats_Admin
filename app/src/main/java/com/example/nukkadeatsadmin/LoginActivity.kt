package com.example.nukkadeatsadmin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nukkadeatsadmin.Modal.UserModal
import com.example.nukkadeatsadmin.databinding.ActivityLoginBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.facebook.AccessToken as AccessToken1
import com.google.firebase.auth.FacebookAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var passw: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager

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
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        //CallBackManager Initialize
        callbackManager = CallbackManager.Factory.create()

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

        //Google Button
        binding.googleButton.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }

        //Facebook Button
        binding.facebookButton.setOnClickListener {

            LoginManager.getInstance().logInWithReadPermissions(
                this,
                listOf("email", "public_profile")
            )

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
            })


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
                startActivity(Intent(this, SignupActivity::class.java))
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

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount = task.result
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                    auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            val user = auth.currentUser
                            saveData(user?.displayName , null , user?.email , null ,"Google")
                            //Successfully signed in with Google
                            Toast.makeText(this, "Signed in with Google", Toast.LENGTH_SHORT).show()
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

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
    }


    // FacebookLogin
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken1) {
        Log.d("Facebook Tag", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Success", "signInWithCredential:success")
                    val user = auth.currentUser
                    saveData(user?.displayName , null , user?.email , null ,"Facebook")
                    updateUi(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Fail", "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUi(null)
                }
            }
    }
}
