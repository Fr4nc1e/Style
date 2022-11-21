package com.example.style.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.style.R
import com.example.style.databinding.ActivityMainBinding
import com.example.style.fragment.HomeFragment
import com.example.style.fragment.NotificationFragment
import com.example.style.fragment.ProfileFragment
import com.example.style.fragment.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var selectedFragment = Fragment()

        binding.bottomNavigation.setOnItemSelectedListener() {
            when (it.itemId) {
                R.id.nav_home -> selectedFragment = HomeFragment()
                R.id.nav_search -> selectedFragment = SearchFragment()
                R.id.nav_add -> startActivity(Intent(this, PostActivity::class.java))
                R.id.nav_heart -> selectedFragment = NotificationFragment()
                R.id.nav_profile -> selectedFragment = ProfileFragment()
            }

            if (!selectedFragment.equals(null)) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit()
            }

            return@setOnItemSelectedListener true
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()
    }
}
