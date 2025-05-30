package com.example.nukkadeatsadmin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nukkadeatsadmin.Modal.UserModal
import com.example.nukkadeatsadmin.databinding.ActivityAdminProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfileActivity : AppCompatActivity() {
    private val binding : ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
        private lateinit var auth : FirebaseAuth
        private lateinit var database : FirebaseDatabase
        private lateinit var adminReference : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adminReference = database.reference.child("Admins")

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.name.isEnabled = false
        binding.address.isEnabled = false
        binding.email.isEnabled = false
        binding.phone.isEnabled = false
        binding.password.isEnabled = false
        binding.imgRes.isEnabled = false
        binding.saveInfoBtn.isEnabled = false

        var isEnable = false
        binding.editButton.setOnClickListener {
            isEnable = !isEnable

            binding.name.isEnabled = isEnable
            binding.address.isEnabled = isEnable
            binding.email.isEnabled = isEnable
            binding.phone.isEnabled = isEnable
            binding.password.isEnabled = isEnable
            binding.imgRes.isEnabled = isEnable


            if(isEnable){
                binding.name.requestFocus()

                binding.imgRes.setOnClickListener{
                    pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
                binding.saveInfoBtn.isEnabled = isEnable
            }
        }

        binding.saveInfoBtn.setOnClickListener {
            updateAdminData()
            finish()
        }
        retrieveAdminData()
    }



    private fun retrieveAdminData() {
        val currentUserUid = auth.currentUser?.uid
        if(currentUserUid != null){
            adminReference = adminReference.child(currentUserUid)
        }

        adminReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var adminName = snapshot.child("name").getValue()
                    var email = snapshot.child("email").getValue()
                    var address = snapshot.child("address").getValue()
                    var phone = snapshot.child("phone").getValue()
                    var password = snapshot.child("password").getValue()

                    setDataTToTextView(adminName , email , address , phone , password)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun setDataTToTextView(adminName: Any?, email: Any?, address: Any? , phone: Any? , password: Any?) {
        binding.name.setText(adminName.toString())
        binding.email.setText(email.toString())
        binding.address.setText(address.toString())
        binding.phone.setText(phone.toString())
        binding.password.setText(password.toString())
    }

    var imageSelected = false
    val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.imgRes.setImageURI(uri)
            imageSelected = true
        }
    }

    private fun updateAdminData() {
        var updateName = binding.name.text.toString()
        var updateEmail = binding.email.text.toString()
        var updateAddress = binding.address.text.toString()
        var updatePhone = binding.phone.text.toString()
        var updatePassword = binding.password.text.toString()



        var adminData = UserModal(updateName , updateAddress , null , updateEmail , updatePhone , updatePassword , null)
        adminReference.setValue(adminData).addOnSuccessListener {

            Toast.makeText(this , "Profile update Successful" , Toast.LENGTH_SHORT).show()
            auth.currentUser?.updateEmail(updateEmail)
            auth.currentUser?.updatePassword(updatePassword)

        }.addOnFailureListener {
            Toast.makeText(this , "Profile update failed" , Toast.LENGTH_SHORT).show()
        }


        //Different Method

//        val currentAdminId = auth.currentUser?.uid
//        if(currentAdminId != null){
//            val adReference = adminReference.child(currentAdminId)
//
//            adReference.child("name").setValue(updateName)
//            adReference.child("address").setValue(updateAddress)
//            adReference.child("email").setValue(updateEmail)
//            adReference.child("phone").setValue(updatePhone)
//            adReference.child("password").setValue(updatePassword)
//
//            Toast.makeText(this , "Profile update failed" , Toast.LENGTH_SHORT).show()
//            auth.currentUser?.updateEmail(updateEmail)
//            auth.currentUser?.updatePassword(updatePassword)
//
//        }else{
//            Toast.makeText(this , "Profile update failed" , Toast.LENGTH_SHORT).show()
//        }
    }
}