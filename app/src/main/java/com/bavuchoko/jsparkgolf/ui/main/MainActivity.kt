package com.bavuchoko.jsparkgolf.ui.main

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.ui.main.game.GameFragment
import com.bavuchoko.jsparkgolf.ui.utils.AuthUtils
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        AuthUtils.checkLoginStatus(this)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_nav_view)

        if (savedInstanceState == null) {
            loadFragment(GameFragment())
            bottomNavigationView.menu.findItem(R.id.fragment_home).setIcon(R.drawable.filled_home)
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null

            selectedFragment?.let { loadFragment(it) }

            true
        }
    }


    override fun onResume() {
        super.onResume()

        AuthUtils.checkLoginStatus(this)
        val sharedPref = getSharedPreferences("golfPreferences", Context.MODE_PRIVATE)
        val jwtToken = sharedPref.getString("accessToken", null)

    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_in_from_right, 0)
        transaction.replace(R.id.content_container, fragment)
        transaction.commit()
    }


}

