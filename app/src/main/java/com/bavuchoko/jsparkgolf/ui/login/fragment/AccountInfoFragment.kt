package com.bavuchoko.jsparkgolf.ui.login.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.ui.login.LoginActivity

class AccountInfoFragment: Fragment() {

    private lateinit var comment: TextView

    private lateinit var joinBtn: TextView
    private lateinit var userName: EditText
    private lateinit var password: EditText
    private lateinit var passwordConfirm: EditText
    private lateinit var passConfirmIcon: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_login_join_account, container, false)
        val prevBtn: TextView = view.findViewById(R.id.prev_btn)
        val name = arguments?.getString("name")
        val birth = arguments?.getString("birth")
        val gender = arguments?.getString("gender")

        comment = view.findViewById(R.id.comment)
        joinBtn = view.findViewById(R.id.join_btn)
        userName = view.findViewById(R.id.sign_up_account_id)
        password = view.findViewById(R.id.sign_up_password)
        passwordConfirm = view.findViewById(R.id.sign_up_password_confirm)
        passConfirmIcon = view.findViewById(R.id.password_check_icon)

        prevBtn.setOnClickListener {
            (activity as? LoginActivity)?.onPrevBtnClicked()
        }

        userName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val inputLength = s?.length ?: 0
                if(inputLength == 11){
                    if( !isValidPhoneNumber(s.toString())){
                        Toast.makeText(context, "올바른 연락처를 입력하세요", Toast.LENGTH_SHORT).show()
                        return
                    }

                    if (password.visibility == View.GONE && inputLength >= 2) {
                        fadeTextChange(comment, "비밀번호를 입력하세요")
                        fadeIn(password)
                    }
                }
                updateNextButtonState(userName, password, passwordConfirm)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val inputText = s?.length ?: 0
                if (passwordConfirm.visibility == View.GONE && inputText >= 3) {
                    fadeTextChange(comment, "비밀번호를 확인하세요")
                    fadeIn(passwordConfirm)
                }
                passCheck(s.toString(), passwordConfirm.text.toString(), passConfirmIcon)
                updateNextButtonState(userName, password, passwordConfirm)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })



        passwordConfirm.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val inputText = s?.length ?: 0
                passCheck(password.text.toString(), s.toString(), passConfirmIcon)
                updateNextButtonState(userName, password, passwordConfirm)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        return view
    }

    fun fadeIn(view: View) {
        view.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(null)
        }
    }

    fun fadeTextChange(textView: TextView, newText: String) {
        textView.animate()
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                textView.text = newText

                textView.alpha = 0f
                textView.translationX = -textView.width.toFloat()

                textView.animate()
                    .alpha(1f)
                    .translationX(0f)
                    .setDuration(300)
                    .start()
            }
            .start()
    }

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val phoneNumberRegex = Regex("^(010|011|016|018)[0-9]{8}$")
        return phoneNumberRegex.matches(phoneNumber)
    }
    fun passCheck(password: String, passwordConfirm: String, icon:ImageView) {
        if(passwordConfirm.length > 0 && password.equals(passwordConfirm)) icon.visibility = View.VISIBLE
        else   icon.visibility = View.GONE
    }

    private fun updateNextButtonState(userName: EditText, password: EditText, passwordConfirm: EditText){
        val userNameStr = userName.text.toString()
        val passwordStr = password.text.toString()
        val passwordConfirmStr = passwordConfirm.text.toString()

        var isValid:Boolean= true
        if(!isValidPhoneNumber(userNameStr)) isValid = false
        if(passwordStr.length<=2) isValid = false
        if(!passwordStr.equals(passwordConfirmStr) ) isValid = false

        joinBtn.isEnabled = isValid
        if (isValid) {
            joinBtn.setTextColor(resources.getColor(R.color.blue, null))
        } else {
            joinBtn.setTextColor(resources.getColor(R.color.disabled_color, null))
        }
    }
}