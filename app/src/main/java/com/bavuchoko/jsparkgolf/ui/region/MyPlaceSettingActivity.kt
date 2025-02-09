package com.bavuchoko.jsparkgolf.ui.region

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.gridlayout.widget.GridLayout
import androidx.lifecycle.ViewModelProvider
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.network.RetrofitFactory
import com.bavuchoko.jsparkgolf.repository.UserRepository
import com.bavuchoko.jsparkgolf.service.UserApiService
import com.bavuchoko.jsparkgolf.viewmodel.UserViewModel
import com.bavuchoko.jsparkgolf.viewmodel.factory.UserViewModelFactory

class MyPlaceSettingActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_place_setting)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val btnBack: Button = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }

        val userApiService = RetrofitFactory.create(this).create(UserApiService::class.java)
        val userRepository = UserRepository(userApiService, this)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository)).get(
            UserViewModel::class.java)

        val gridLayout = findViewById<GridLayout>(R.id.grid_sido)
        val sidoList = listOf(
            "서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시",
            "대전광역시", "울산광역시", "세종특별자치시", "경기도", "강원도",
            "충청북도", "충청남도", "전라북도", "전라남도", "경상북도",
            "경상남도", "제주특별자치도"
        )

        for (sido in sidoList) {
            val btnMyPlace = Button(this).apply {
                text = sido
                setPadding(6, 24, 6, 24)
                gravity = Gravity.CENTER
                setBackgroundResource(R.color.app_color)
                setTextColor(Color.WHITE)
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    setMargins(16, 16, 16, 16)
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                }
            }

            btnMyPlace.setOnClickListener {
                // 버튼 클릭 시 해당 시도 값 사용
                val selectedSido = (it as Button).text.toString()
                userViewModel.saveRegion(selectedSido)
                userViewModel.userRegion.observe(this){ region ->
                    if (region != null) {
                        finish()
                    } else {

                    }
                }
            }

            gridLayout.addView(btnMyPlace)
        }
    }
}