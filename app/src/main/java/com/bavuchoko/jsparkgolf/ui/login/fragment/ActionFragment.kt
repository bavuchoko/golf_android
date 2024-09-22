package com.bavuchoko.jsparkgolf.ui.login.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.network.RetrofitFactory
import com.bavuchoko.jsparkgolf.repository.UserRepository
import com.bavuchoko.jsparkgolf.ui.MainActivity
import com.bavuchoko.jsparkgolf.viewmodel.UserViewModel
import com.bavuchoko.jsparkgolf.viewmodel.factory.UserViewModelFactory

class ActionFragment : Fragment() {

    lateinit var usernameEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var loginButton: TextView
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login_action, container, false)
        view.setBackgroundColor(resources.getColor(R.color.white))

        val userRepository = UserRepository(RetrofitFactory.createUserApiService(requireContext()))
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository)).get(UserViewModel::class.java)

        usernameEditText = view.findViewById<EditText>(R.id.et_username)
        passwordEditText = view.findViewById(R.id.et_password)
        loginButton = view.findViewById(R.id.btn_login)


        usernameEditText.requestFocus()
        usernameEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        // 로그인 버튼 클릭 리스너
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            userViewModel.login(username, password)
        }

        userViewModel.jwtToken.observe(viewLifecycleOwner) { token ->
            if (token != null) {
                val sharedPref = activity?.getSharedPreferences("golfPreferences", Context.MODE_PRIVATE)
                val editor = sharedPref?.edit()
                editor?.putString("accessToken", token)
                editor?.apply()

                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                activity?.finish()
            } else {
                // 로그인 실패 처리
            }
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}