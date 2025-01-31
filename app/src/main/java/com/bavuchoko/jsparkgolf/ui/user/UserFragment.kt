package com.bavuchoko.jsparkgolf.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.common.CommonMethod
import com.bavuchoko.jsparkgolf.ui.MainActivity
import com.bavuchoko.jsparkgolf.ui.utils.AuthUtils


class UserFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    private lateinit var userNameTextView: TextView
    lateinit var logoutBtn : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = context as MainActivity
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        logoutBtn = view.findViewById(R.id.log_out_btn)
        val accessToken = CommonMethod.getToken(mainActivity)
        val userName = CommonMethod.getValue(requireContext(),"name")

        userNameTextView = view.findViewById(R.id.user_name)
        userNameTextView.text = userName



        logoutBtn.setOnClickListener {
            CommonMethod.clearAllValue(mainActivity)
            AuthUtils.checkLoginStatus(mainActivity)
        }
        return view
    }

}