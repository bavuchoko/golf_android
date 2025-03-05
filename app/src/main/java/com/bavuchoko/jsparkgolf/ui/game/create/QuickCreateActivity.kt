package com.bavuchoko.jsparkgolf.ui.game.create

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.component.game.create.GameCreateButtonHandler

class QuickCreateActivity : AppCompatActivity()  {

    private lateinit var btnBack: Button
    private lateinit var companionPanel: TextView
    private lateinit var inputCompanion : EditText
    private lateinit var tempUserContainer: LinearLayout
    private lateinit var btnCreate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quick_create)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        btnBack = findViewById(R.id.btn_back)
        btnCreate = findViewById(R.id.btn_create)

        companionPanel = findViewById(R.id.panel_input_companion)
        inputCompanion = findViewById(R.id.input_companion)
        tempUserContainer = findViewById(R.id.temp_user_container)

        companionPanel.scaleX = 0.5f
        companionPanel.scaleY = 0.5f
        companionPanel.visibility = View.VISIBLE

        companionPanel.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(300)
            .withEndAction {
                companionPanel.postDelayed({
                    inputCompanion.visibility = View.VISIBLE
                    inputCompanion.translationY = 50f
                    inputCompanion.alpha = 0f
                    inputCompanion.animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setDuration(300)
                    .setInterpolator(DecelerateInterpolator())
                    .start()
                    inputCompanion.requestFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(inputCompanion, InputMethodManager.SHOW_IMPLICIT)
                }, 300)
            }
            .start()

        val buttonHandler = GameCreateButtonHandler(this,btnBack)
        buttonHandler.setupButtonListeners()

        inputCompanion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // 함수 호출
                validateInput(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }

    private fun validateInput(input: String) {
        if (input.isNotEmpty()) {
            try {
                val number = input.toInt()
                var players = number
                if (number > 3) {
                    Toast.makeText(this,"동반자는 최대 3명 입니다.", Toast.LENGTH_SHORT).show()
                    inputCompanion.setText("3")
                    inputCompanion.setSelection(inputCompanion.length())
                    players = 3
                }
                else if (number < 0) {
                    inputCompanion.setText("0")
                    inputCompanion.setSelection(inputCompanion.length())
                    players = 0
                }
                addDynamicEditTexts(players)
            } catch (e: NumberFormatException) {
                inputCompanion.setText("")
            }
        }
    }

    private fun addDynamicEditTexts(count: Int) {
        btnCreate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        btnCreate.setTextColor(resources.getColor(R.color.blue))
        tempUserContainer.removeAllViews()

        val inflater = layoutInflater

        for (i in 1..count) {
            val editTextView = inflater.inflate(R.layout.temp_user_name, tempUserContainer, false) as EditText
            editTextView.hint = "동반자 ${i} 이름 입력"
            tempUserContainer.addView(editTextView)
        }
    }


}