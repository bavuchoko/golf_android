package com.bavuchoko.jsparkgolf.ui.game.view

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.common.CommonMethod.getValue
import com.bavuchoko.jsparkgolf.component.game.view.GameViewButtonHandler
import com.bavuchoko.jsparkgolf.network.RetrofitFactory
import com.bavuchoko.jsparkgolf.repository.GameRepository
import com.bavuchoko.jsparkgolf.service.GameApiService
import com.bavuchoko.jsparkgolf.ui.game.view.fragment.CurrentRoundFragment
import com.bavuchoko.jsparkgolf.ui.game.view.fragment.ScoringRoundFragment
import com.bavuchoko.jsparkgolf.ui.game.view.fragment.TotalScoreFragment
import com.bavuchoko.jsparkgolf.viewmodel.GameViewModel
import com.bavuchoko.jsparkgolf.viewmodel.factory.GameViewModelFactory
import com.google.android.material.tabs.TabLayout

class GameViewActivity : AppCompatActivity() {

    private lateinit var btnBack: Button
    private lateinit var tabLayout: TabLayout
    private lateinit var gameViewModel: GameViewModel
    private var isHost: Boolean = false // 게임 호스트 여부 (Boolean)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_view)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        initializeViews()
        setupButtonListeners()
        tabLayout.getTabAt(0)?.select()
        val gameId = intent.getLongExtra("gameId", -1L)
        if (gameId != -1L) {
            getGameDetails(gameId)
        }
    }

    private fun initializeViews() {
        btnBack = findViewById(R.id.btn_back)
        tabLayout = findViewById(R.id.tab_layout)
        val gameRepository = GameRepository(RetrofitFactory.create(this).create(GameApiService::class.java))
        gameViewModel = ViewModelProvider(this, GameViewModelFactory(gameRepository)).get(GameViewModel::class.java)
    }

    private fun setupButtonListeners() {
        val buttonHandler = GameViewButtonHandler(this, btnBack)
        buttonHandler.setupButtonListeners()
    }

    private fun getGameDetails(gameId: Long) {
        gameViewModel.getGameById(gameId)
        gameViewModel.gameView.observe(this) { game ->
            game?.let {
                val userId = getValue(this, "id")?.toLong()
                if (userId != null && game.host.id == userId) {
                    isHost = true
                }
                setupTabsBasedOnHost()
                Log.d("GameViewActivity", "게임 정보: ${game.id}, Host 여부: $isHost")
            }
        }
    }

    private fun setupTabsBasedOnHost() {
        tabLayout.removeAllTabs() // 기존 탭 제거

        if (isHost) {
            tabLayout.addTab(tabLayout.newTab().setText("점수 입력"))
            tabLayout.addTab(tabLayout.newTab().setText("현재 라운드 보기"))
            tabLayout.addTab(tabLayout.newTab().setText("전체 점수 보기"))
        } else {
            tabLayout.addTab(tabLayout.newTab().setText("현재 라운드 보기"))
            tabLayout.addTab(tabLayout.newTab().setText("전체 점수 보기"))
        }

        if (isHost) {
            replaceFragment(ScoringRoundFragment())
        } else {
            replaceFragment(CurrentRoundFragment())
        }

        // 탭 선택 리스너 설정
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    when (it.position) {
                        0 -> {
                            if (isHost) {
                                replaceFragment(ScoringRoundFragment())
                            } else {
                                replaceFragment(CurrentRoundFragment())
                            }
                        }
                        1 -> {
                            if (isHost) {
                                replaceFragment(CurrentRoundFragment())
                            } else {
                                replaceFragment(TotalScoreFragment())
                            }
                        }
                        2 -> {
                            if (isHost) {
                                replaceFragment(TotalScoreFragment())
                            }
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }


    private fun replaceFragment(fragment: Fragment) {
        Log.d("GameViewActivity", "Fragment 교체: ${fragment::class.java.simpleName}")
        supportFragmentManager.beginTransaction()
            .replace(R.id.game_view_fragment, fragment)
            .commit()
    }
}
