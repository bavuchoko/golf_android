package com.bavuchoko.jsparkgolf.ui.game.view

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.common.CommonMethod.getValue
import com.bavuchoko.jsparkgolf.component.game.view.GameViewButtonHandler
import com.bavuchoko.jsparkgolf.network.RetrofitFactory
import com.bavuchoko.jsparkgolf.repository.GameRepository
import com.bavuchoko.jsparkgolf.repository.ScoreRepository
import com.bavuchoko.jsparkgolf.service.GameApiService
import com.bavuchoko.jsparkgolf.service.ScoreService
import com.bavuchoko.jsparkgolf.ui.game.view.fragment.CurrentRoundFragment
import com.bavuchoko.jsparkgolf.ui.game.view.fragment.ScoringRoundFragment
import com.bavuchoko.jsparkgolf.ui.game.view.fragment.TotalScoreFragment
import com.bavuchoko.jsparkgolf.viewmodel.GameViewModel
import com.bavuchoko.jsparkgolf.viewmodel.ScoreViewModel
import com.bavuchoko.jsparkgolf.viewmodel.factory.GameViewModelFactory
import com.bavuchoko.jsparkgolf.viewmodel.factory.ScoreViewModelFactory
import com.bavuchoko.jsparkgolf.vo.GameVo
import com.bavuchoko.jsparkgolf.vo.ScoreVo
import com.google.android.material.tabs.TabLayout

class GameViewActivity : AppCompatActivity(), ScoringRoundFragment.ScoreUpdateListener  {

    private lateinit var btnBack: Button
    private lateinit var tabLayout: TabLayout
    private lateinit var gameViewModel: GameViewModel
    private lateinit var scoreViewModel: ScoreViewModel
    private lateinit var btnNext: TextView
    private var isHost: Boolean = false
    private var isPlaying: Boolean = false
    private var nextable: Boolean = false
    private lateinit var game:GameVo

    private val scoresForSave: MutableList<ScoreVo> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_view)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        btnNext = findViewById(R.id.btn_next)

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
        tabLayout.setTabRippleColorResource(android.R.color.transparent)
        tabLayout.setSelectedTabIndicatorColor(resources.getColor(android.R.color.black))
        tabLayout.setTabTextColors(
            resources.getColor(R.color.deep_gray),  // 기본 텍스트 색상
            resources.getColor(R.color.app_color)   // 선택된 텍스트 색상
        )
        val gameRepository = GameRepository(RetrofitFactory.create(this).create(GameApiService::class.java))
        val scoreRepository = ScoreRepository(RetrofitFactory.create(this).create(ScoreService::class.java))
        gameViewModel = ViewModelProvider(this, GameViewModelFactory(gameRepository)).get(GameViewModel::class.java)
        scoreViewModel = ViewModelProvider(this, ScoreViewModelFactory(scoreRepository)).get(ScoreViewModel::class.java)
    }

    private fun setupButtonListeners() {
        val buttonHandler = GameViewButtonHandler(this, btnBack)
        buttonHandler.setupButtonListeners()

        btnNext.setOnClickListener {
            if (nextable) {
                updateGameProgress()
            }else{

            }
        }
    }

    private fun updateGameProgress() {
        if (isPlaying) {
            scoreViewModel.saveScores(game.id, scoresForSave)
        } else {
            Log.d("GameViewActivity", "게임이 진행 중이 아닙니다.")
        }
    }

    private fun getGameDetails(gameId: Long) {
        gameViewModel.getGameById(gameId)
        gameViewModel.gameView.observe(this) { resoponse ->
            resoponse?.let {
                game = resoponse
                val userId = getValue(this, "id")?.toLong()
                if (userId != null && resoponse.host.id == userId) {
                    isHost = true
                }
                if(resoponse.progress.state =="PLAYING"){
                    isPlaying = true
                }

                setupTabsBasedOnHost(resoponse)
                Log.d("GameViewActivity", "게임 정보: ${resoponse.id}, Host 여부: $isHost")
            }
        }
    }

    private fun setupTabsBasedOnHost(game: GameVo) {
        tabLayout.removeAllTabs()

        if (isHost) {
            tabLayout.addTab(tabLayout.newTab().setText("점수입력"))
            tabLayout.addTab(tabLayout.newTab().setText("현재점수"))
            tabLayout.addTab(tabLayout.newTab().setText("전체점수"))
        } else {
            tabLayout.addTab(tabLayout.newTab().setText("현재점수"))
            tabLayout.addTab(tabLayout.newTab().setText("전체점수"))
        }

        if (isHost) {
            replaceFragment(ScoringRoundFragment(), game)
        } else {
            replaceFragment(CurrentRoundFragment(), game)
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                tab?.let {
                    tab.view.setBackgroundColor(ContextCompat.getColor(this@GameViewActivity, android.R.color.transparent))
                    when (it.position) {
                        0 -> {

                            if (isHost) {
                                replaceFragment(ScoringRoundFragment(), game)
                            } else {
                                replaceFragment(CurrentRoundFragment(), game)
                            }
                        }
                        1 -> {
                            if (isHost) {
                                replaceFragment(CurrentRoundFragment(), game)
                            } else {
                                replaceFragment(TotalScoreFragment(), game)
                            }
                        }
                        2 -> {
                            if (isHost) {
                                replaceFragment(TotalScoreFragment(), game)
                            }
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {
                    // 선택되지 않은 탭에 대해 배경을 투명하게 설정
                    tab.view.setBackgroundColor(ContextCompat.getColor(this@GameViewActivity, android.R.color.transparent))
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun replaceFragment(fragment: Fragment, game: GameVo) {
        Log.d("GameViewActivity", "Fragment 교체: ${fragment::class.java.simpleName}")

        val bundle = Bundle().apply {
            putParcelable("game", game)
        }
        fragment.arguments = bundle


        if (fragment is ScoringRoundFragment) {
            fragment.setScoreUpdateListener(this)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.game_view_container, fragment)
            .commit()
    }

    override fun onScoreUpdated(scoreList: List<ScoreVo>) {
        scoresForSave.clear()
        scoresForSave.addAll(scoreList)
        Log.d("GameViewActivity", "Scores for save: $scoresForSave")

        checkIfNextable()
    }

    private fun checkIfNextable() {
        val allPlayersHaveScores = game.players.all { player ->
            // 모든 선수에 대해서 점수 입력
            scoresForSave.any { it.playerId == player.id }
        }
        //입력된 선수 모드 hit > 0
        val allHitsGreaterThanZero = scoresForSave.all { it.hit > 0 }

        nextable = allPlayersHaveScores && allHitsGreaterThanZero
        if (nextable) {
            btnNext.setTextColor(getResources().getColor(R.color.blue))
            Log.d("GameViewActivity", "다음 단계로 진행 가능합니다.")
        } else {
            btnNext.setTextColor(getResources().getColor(R.color.gray))
            Log.d("GameViewActivity", "모든 점수가 입력되지 않았거나 hit 값이 0인 플레이어가 있습니다.")
        }
    }
}
