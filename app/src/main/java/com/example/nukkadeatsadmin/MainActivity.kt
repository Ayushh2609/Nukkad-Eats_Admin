package com.example.nukkadeatsadmin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nukkadeatsadmin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
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

        binding.addMenu.setOnClickListener{
            startActivity(Intent(this , AddMenuActivity::class.java))

        }

        binding.addAllItem.setOnClickListener{

        }

        binding.outForDelivery.setOnClickListener{

        }

        binding.feedback.setOnClickListener{

        }

        binding.profile.setOnClickListener{

        }

        binding.moneyOnHold.setOnClickListener{

        }

        binding.createNewUser.setOnClickListener{

        }

        binding.logout.setOnClickListener{

        }


    }
}