package com.brm.brmlabkotlin.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.brm.brmlabkotlin.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomView = findViewById<BottomNavigationView>(R.id.activity_main_bottom_nav)

        setupActionBarWithNavController(findNavController(R.id.mainNavHost))
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainNavHost) as NavHostFragment?
        NavigationUI.setupWithNavController(
            bottomView,
            navHostFragment!!.navController
        )
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.mainNavHost)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}