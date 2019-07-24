package com.afedaxo.presentation.ui.main

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.afedaxo.R
import com.afedaxo.presentation.ui.base.BaseActivity

class MainActivity : BaseActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,null)
    }
}