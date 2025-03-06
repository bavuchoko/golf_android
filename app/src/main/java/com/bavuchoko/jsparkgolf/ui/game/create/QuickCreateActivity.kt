package com.bavuchoko.jsparkgolf.ui.game.create

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.View
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.common.CommonMethod
import com.bavuchoko.jsparkgolf.component.game.create.GameCreateButtonHandler
import com.bavuchoko.jsparkgolf.dto.request.QuickGameRequestDto
import com.bavuchoko.jsparkgolf.network.RetrofitFactory
import com.bavuchoko.jsparkgolf.repository.GameRepository
import com.bavuchoko.jsparkgolf.repository.UserRepository
import com.bavuchoko.jsparkgolf.service.GameApiService
import com.bavuchoko.jsparkgolf.service.UserApiService
import com.bavuchoko.jsparkgolf.ui.game.view.GameViewActivity
import com.bavuchoko.jsparkgolf.viewmodel.GameViewModel
import com.bavuchoko.jsparkgolf.viewmodel.UserViewModel
import com.bavuchoko.jsparkgolf.viewmodel.factory.GameViewModelFactory
import com.bavuchoko.jsparkgolf.viewmodel.factory.UserViewModelFactory

class QuickCreateActivity : AppCompatActivity()  {

    private lateinit var gameViewModel: GameViewModel

    private lateinit var btnBack: Button
    private lateinit var companionPanel: TextView
    private lateinit var tempUserText: TextView
    private lateinit var inputCompanion : EditText
    private lateinit var tempUserContainer: LinearLayout
    private lateinit var btnCreate: TextView
    private var test: Int = 0
    private lateinit var userViewModel: UserViewModel
    private var tempUserNames: Array<String> = arrayOf()
    private var creattable: Boolean = false         // 시작하기 버튼 클릭 가능여부

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quick_create)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // ViewModel 초기화
        val gameService = RetrofitFactory.create(this).create(GameApiService::class.java)
        val gameRepository = GameRepository(gameService)
        gameViewModel = ViewModelProvider(this, GameViewModelFactory(gameRepository)).get(GameViewModel::class.java)

        btnBack = findViewById(R.id.btn_back)
        btnCreate = findViewById(R.id.btn_create)
        tempUserText = findViewById(R.id.temp_user_text)

        companionPanel = findViewById(R.id.panel_input_companion)
        inputCompanion = findViewById(R.id.input_companion)
        tempUserContainer = findViewById(R.id.temp_user_container)


        btnCreate.setOnClickListener {
            doQuickStart()
        }

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


        val userApiService = RetrofitFactory.create(this).create(UserApiService::class.java)
        val userRepository = UserRepository(userApiService, this)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository)).get(UserViewModel::class.java)

        gameViewModel.gameView.observe(this, Observer { gameVo ->
            gameVo?.let {
                val intent = Intent(this, GameViewActivity::class.java)
                intent.putExtra("gameVo", it)
                startActivity(intent)
            }
        })

        // 오류 처리
        gameViewModel.error.observe(this, Observer { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun validateInput(input: String) {

        if (input.isNotEmpty()) {
            try {
                val number = input.toInt()
                var players = number
                if (number > 3) {
                    val toast = Toast.makeText(this, "동반자는 최대 3명 입니다.", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.TOP, 0, 10)
                    toast.show()
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

    private fun checkCreatBtn(value: Boolean) {
        if(value){
            btnCreate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            btnCreate.setTextColor(resources.getColor(R.color.blue))
            creattable = true
        }else{
            btnCreate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            btnCreate.setTextColor(resources.getColor(R.color.gray))
            creattable = false
        }

    }

    private fun addDynamicEditTexts(count: Int) {
        checkCreatBtn(true)
        val currentChildCount = if (test == 0) tempUserContainer.childCount else test
        test = count

        val inflater = layoutInflater

        if (count > currentChildCount) {
            if (tempUserText.visibility == View.GONE) {
                tempUserText.visibility = View.VISIBLE
                tempUserText.translationY = 50f
                tempUserText.alpha = 0f
                tempUserText.animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setDuration(300)
                    .setStartDelay(50)
                    .setInterpolator(DecelerateInterpolator())
                    .withEndAction {
                        for (i in currentChildCount until count) {
                            val editTextView = inflater.inflate(R.layout.temp_user_name, tempUserContainer, false) as EditText
                            editTextView.hint = "동반자 ${i + 1} 이름 입력"

                            editTextView.translationX = -tempUserContainer.width.toFloat()
                            tempUserContainer.addView(editTextView)

                            editTextView.animate()
                                .translationX(0f)
                                .setDuration(300)
                                .setStartDelay(100 * (i + 1).toLong())
                                .setInterpolator(OvershootInterpolator())
                                .start()
                        }
                    }
                    .start()
            } else {
                for (i in currentChildCount until count) {
                    val editTextView = inflater.inflate(R.layout.temp_user_name, tempUserContainer, false) as EditText
                    editTextView.hint = "동반자 ${i + 1} 이름 입력"

                    editTextView.translationX = -tempUserContainer.width.toFloat()
                    tempUserContainer.addView(editTextView)

                    editTextView.animate()
                        .translationX(0f)
                        .setDuration(300)
                        .setStartDelay(100 * (i + 1).toLong())
                        .setInterpolator(OvershootInterpolator())
                        .start()
                }
            }
        } else if (count < currentChildCount) {
            val excessCount = currentChildCount - count
            for (i in 0 until excessCount) {
                val child = tempUserContainer.getChildAt(tempUserContainer.childCount - 1 - i)
                child.animate()
                    .translationX(-tempUserContainer.width.toFloat())
                    .setDuration(600)
                    .setStartDelay(150 * i.toLong())
                    .withEndAction {
                        tempUserContainer.removeView(child)
                    }
                    .start()
            }
        }
    }





    private fun doQuickStart() {
        if (creattable) {
            val regionCode: String? = CommonMethod.getRegionCode(this);

            val userNames = mutableListOf<String>()

            for (i in 0 until tempUserContainer.childCount) {
                val childView = tempUserContainer.getChildAt(i)
                if (childView is EditText) {
                    var userName = childView.text.toString()
                    if(userName.isEmpty()) userName = "동반_"+i
                    userNames.add(userName)
                }
            }
            val quickGameRequestDto = QuickGameRequestDto(null, regionCode, userNames.toTypedArray());
            gameViewModel.quickStartGame(quickGameRequestDto)

        }
    }


}