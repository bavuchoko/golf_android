package com.bavuchoko.jsparkgolf.ui.login.fragment

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.ui.login.LoginActivity

class IndividualInfoFragment: Fragment() {

    private lateinit var comment: TextView

    private lateinit var name:EditText
    private lateinit var birth:EditText
    private lateinit var genderSelector:RadioGroup
    private lateinit var nextBtn:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_login_join_individual, container, false)
        val prevBtn: ImageButton = view.findViewById(R.id.back_button)


        comment = view.findViewById(R.id.comment)
        name = view.findViewById(R.id.sign_up_name)
        birth = view.findViewById(R.id.sign_up_birt)
        genderSelector = view.findViewById(R.id.gender_selector)
        nextBtn = view.findViewById(R.id.next_btn)

        prevBtn.setOnClickListener {
            (activity as? LoginActivity)?.onPrevBtnClicked()
        }
        nextBtn.setOnClickListener {
            val enteredName = name.text.toString()
            val enteredBirth = birth.text.toString()
            val selectedGenderId = genderSelector.checkedRadioButtonId
            val genderText = when (selectedGenderId) {
                R.id.sign_up_male -> "MALE"
                R.id.sign_up_female -> "FEMALE"
                else -> "MALE"
            }
            val nextFragment = containFragmentData(enteredName, enteredBirth, genderText)


            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, nextFragment)
                ?.addToBackStack(null)
                ?.commit()
        }


        name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val inputText = s?.length ?: 0
                if (birth.visibility == View.GONE && inputText >= 2) {
                    fadeTextChange(comment, "생일을 입력하세요")
                    fadeIn(birth)
                }

                updateNextButtonState(name, birth, genderSelector)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        birth.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val inputText = s?.toString() ?: ""
                if (inputText.length >= 6) {  // 6글자 이상일 때만 호출
                    val birthRegex = Regex("^[0-9]{6}$")
                    if (birthRegex.matches(inputText) && isValidBirthDate(inputText)) {
                        if (genderSelector.visibility == View.GONE) {
                            fadeTextChange(comment, "성별을 선택하세요")
                            fadeIn(genderSelector)
                        }
                    } else {
                        // 유효하지 않은 날짜일 때, Toast 표시
                        Toast.makeText(context, "올바른 생년월일을 입력하세요", Toast.LENGTH_SHORT).show()
                    }
                }
                updateNextButtonState(name, birth, genderSelector)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        genderSelector.setOnCheckedChangeListener { group, checkedId ->
            updateNextButtonState(name, birth, genderSelector)
        }

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
        // 애니메이터를 정의하여 텍스트를 서서히 사라지게 함
        textView.animate()
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                textView.text = newText

                // 새 텍스트를 왼쪽에서 오른쪽으로 슬라이드 인하게 설정
                textView.alpha = 0f
                textView.translationX = -textView.width.toFloat() // 시작 위치: 왼쪽 바깥

                textView.animate()
                    .alpha(1f)
                    .translationX(0f) // 끝 위치: 원래 위치
                    .setDuration(300)
                    .start()
            }
            .start()
    }


    private fun isValidBirthDate(birthText: String): Boolean {
        try {
            // yymmdd에서 yy와 mm, dd를 추출
            val year = birthText.substring(0, 2).toInt()
            val month = birthText.substring(2, 4).toInt()
            val day = birthText.substring(4, 6).toInt()

            // 1940년대부터 2099년까지 허용
            val fullYear = if (year in 40..99) 1900 + year else 2000 + year

            // 월별 일 수 확인
            if (month < 1 || month > 12) return false
            val daysInMonth = when (month) {
                2 -> if (isLeapYear(fullYear)) 29 else 28  // 윤년 계산
                4, 6, 9, 11 -> 30  // 4월, 6월, 9월, 11월은 30일까지만 존재
                else -> 31  // 그 외 달은 31일까지 존재
            }

            // 입력된 날짜가 해당 월의 최대 일 수를 넘지 않는지 확인
            return day in 1..daysInMonth
        } catch (e: Exception) {
            return false
        }
    }

    // 윤년 계산 함수
    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    fun containFragmentData(name: String, birth: String, gender: String): Fragment {
        val bundle = Bundle().apply {
            putString("name", name)
            putString("birth", birth)
            putString("gender", gender)
        }

        val nextFragment = AccountInfoFragment()
        nextFragment.arguments = bundle

        return nextFragment
    }

    private fun updateNextButtonState(nameInput:EditText, birthInput:EditText, genderRadio:RadioGroup){
        val enteredName = nameInput.text.toString()
        val enteredBirth = birthInput.text.toString()
        val selectedGender = genderRadio.checkedRadioButtonId
        val genderText = when (selectedGender) {
            R.id.sign_up_male -> "MALE"
            R.id.sign_up_female -> "FEMALE"
            else -> ""
        }
        var isValid:Boolean= true
        if(enteredName.length<=2) isValid = false
        if(!isValidBirthDate(enteredBirth)) isValid = false
        if(!listOf("MALE","FEMALE").contains(genderText) ) isValid = false

        nextBtn.isEnabled = isValid
        if (isValid) {
            nextBtn.setTextColor(resources.getColor(R.color.blue, null))
        } else {
            nextBtn.setTextColor(resources.getColor(R.color.disabled_color, null))
        }
    }
}