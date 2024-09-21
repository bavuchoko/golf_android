package com.bavuchoko.jsparkgolf.ui.login.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.ui.login.LoginActivity

class MainFragment  : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login_main, container, false)

       view.findViewById<TextView>(R.id.btn_login_jump)


        view.findViewById<TextView>(R.id.btn_login_jump).setOnClickListener {
            (activity as? LoginActivity)?.navigateToActionFragment()
        }

        return view
    }



}