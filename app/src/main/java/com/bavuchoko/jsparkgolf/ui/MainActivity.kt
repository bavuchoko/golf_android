package com.bavuchoko.jsparkgolf.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.ui.competition.CompetitionFragment
import com.bavuchoko.jsparkgolf.ui.field.FieldFragment
import com.bavuchoko.jsparkgolf.ui.game.GameFragment
import com.bavuchoko.jsparkgolf.ui.user.UserFragment
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
            bottomNavigationView.menu.findItem(R.id.fragment_home).setIcon(R.drawable.ic_home_filled)
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null

            when (item.itemId) {
                R.id.fragment_home -> {
                    selectedFragment = GameFragment()
                    // 아이콘 변경
                    bottomNavigationView.menu.findItem(R.id.fragment_home).setIcon(R.drawable.ic_home_filled)
                }
                R.id.fragment_field -> {
                    selectedFragment = FieldFragment()
                    // 아이콘 변경
                    bottomNavigationView.menu.findItem(R.id.fragment_field).setIcon(R.drawable.ic_place_filled) // 비슷하게 변경
                }
                R.id.fragment_competition -> {
                    selectedFragment = CompetitionFragment()
                    // 아이콘 변경
                    bottomNavigationView.menu.findItem(R.id.fragment_competition).setIcon(R.drawable.ic_trophy_filled) // 비슷하게 변경
                }
                R.id.fragment_user -> {
                    selectedFragment = UserFragment()
                    // 아이콘 변경
                    bottomNavigationView.menu.findItem(R.id.fragment_user).setIcon(R.drawable.ic_user_filled) // 비슷하게 변경
                }
            }

            for (menuItem in bottomNavigationView.menu) {
                if (menuItem.itemId != item.itemId) {
                    when (menuItem.itemId) {
                        R.id.fragment_home -> menuItem.setIcon(R.drawable.ic_home)
                        R.id.fragment_field -> menuItem.setIcon(R.drawable.ic_place)
                        R.id.fragment_competition -> menuItem.setIcon(R.drawable.ic_trophy)
                        R.id.fragment_user -> menuItem.setIcon(R.drawable.ic_user)
                    }
                }
            }

            selectedFragment?.let { loadFragment(it) }

            true
        }
    }


    override fun onResume() {
        super.onResume()

        AuthUtils.checkLoginStatus(this)
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_in_from_right, 0)
        transaction.replace(R.id.content_container, fragment)
        transaction.commit()
    }
}
