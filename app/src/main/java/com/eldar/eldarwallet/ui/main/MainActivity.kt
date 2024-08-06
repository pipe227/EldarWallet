package com.eldar.eldarwallet.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.eldar.eldarwallet.R
import com.eldar.eldarwallet.databinding.ActivityMainBinding
import com.eldar.eldarwallet.ui.home.HomeFragment
import com.eldar.eldarwallet.ui.payment.PaymentFragment
import com.eldar.eldarwallet.ui.qr.QRFragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigationBar()

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationBar = findViewById<ChipNavigationBar>(R.id.bottomNavigationBar)
        bottomNavigationBar.setOnItemSelectedListener { id ->
            when (id) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                }
                R.id.nav_payment -> {
                    loadFragment(PaymentFragment())
                }
                R.id.nav_qr -> {
                    loadFragment(QRFragment())
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}


