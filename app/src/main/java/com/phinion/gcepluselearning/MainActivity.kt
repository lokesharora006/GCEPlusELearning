package com.phinion.gcepluselearning

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.phinion.gcepluselearning.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        
        binding.contactBtn.setOnClickListener {
            startActivity(Intent(this, ContactActivity::class.java))
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
        navController = Navigation.findNavController(this, R.id.navHostFragment)
        setupWithNavController(binding.bottomNavigationView, navController)



    }
}
