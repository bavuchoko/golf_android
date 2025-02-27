package com.bavuchoko.jsparkgolf.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.ui.login.fragment.ActionFragment
import com.bavuchoko.jsparkgolf.ui.login.fragment.IndividualInfoFragment
import com.bavuchoko.jsparkgolf.ui.login.fragment.MainFragment

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment())
                .commit()
        }
    }


    fun navigateToActionFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in_from_right, 0)
            .replace(R.id.fragment_container, ActionFragment())
            .addToBackStack(null)
            .commit()
    }

    fun navigateToJoinFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in_from_right, 0)
            .replace(R.id.fragment_container, IndividualInfoFragment())
            .addToBackStack(null)
            .commit()
    }
    fun onPrevBtnClicked() {
        if (supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_out_to_right, 0) // 애니메이션 추가
                .commit()
            supportFragmentManager.popBackStackImmediate()
        }
//        supportFragmentManager.popBackStack()
    }
}