package com.bavuchoko.jsparkgolf.ui.login.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.common.CommonMethod
import com.bavuchoko.jsparkgolf.dialog.ApiLoading
import com.bavuchoko.jsparkgolf.network.RetrofitFactory
import com.bavuchoko.jsparkgolf.repository.UserRepository
import com.bavuchoko.jsparkgolf.ui.MainActivity
import com.bavuchoko.jsparkgolf.ui.login.LoginActivity
import com.bavuchoko.jsparkgolf.viewmodel.UserViewModel
import com.bavuchoko.jsparkgolf.viewmodel.factory.UserViewModelFactory

class ActionFragment : Fragment() {

    lateinit var loginActivity: LoginActivity
    lateinit var usernameEditText: EditText
    lateinit var backButton: ImageButton
    lateinit var passwordEditText: EditText
    lateinit var loginButton: TextView
    private lateinit var userViewModel: UserViewModel
    private lateinit var loading: ApiLoading

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginActivity = context as LoginActivity
        val view = inflater.inflate(R.layout.fragment_login_action, container, false)
        view.setBackgroundColor(resources.getColor(R.color.white))

        val userRepository = UserRepository(RetrofitFactory.createUserApiService(requireContext()))
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository)).get(UserViewModel::class.java)

        usernameEditText = view.findViewById<EditText>(R.id.et_username)
        passwordEditText = view.findViewById(R.id.et_password)
        loginButton = view.findViewById(R.id.btn_login)
        backButton = view.findViewById(R.id.back_button)
        loading = ApiLoading(requireContext())
        usernameEditText.requestFocus()
        usernameEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        // 로그인 버튼 클릭 리스너
        loginButton.setOnClickListener {
            loading.show()
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            userViewModel.login(username, password)
        }

        backButton.setOnClickListener{
            (activity as? LoginActivity)?.onPrevBtnClicked()
        }

        userViewModel.jwtToken.observe(viewLifecycleOwner) { token ->
            if (token != null) {
                CommonMethod.saveAccessToken(loginActivity, token)

                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                loading.disappear()
                activity?.finish()
            } else {
                loading.disappear()
                showErrorDialog("네트워크 에러가 발생하였습니다\n관리자에게 문의하세요.")
//                Toast.makeText(context, "네트워크 에러로 로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showErrorDialog(message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("요청 실패")
        builder.setMessage(message)
        builder.setPositiveButton("확인") { dialog, _ ->
            dialog.dismiss()  // 대화상자 닫기
        }
        val dialog = builder.create()
        dialog.show()
    }
}
