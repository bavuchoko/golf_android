package com.bavuchoko.jsparkgolf.ui.game.view

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
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
import com.bavuchoko.jsparkgolf.vo.GameVo
import com.bavuchoko.jsparkgolf.vo.ScoreVo
import com.google.android.material.tabs.TabLayout

class GameViewActivity : AppCompatActivity(), ScoringRoundFragment.ScoreUpdateListener  {

    private lateinit var btnBack: Button
    private lateinit var tabLayout: TabLayout
    private lateinit var gameViewModel: GameViewModel
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
        val gameRepository = GameRepository(RetrofitFactory.create(this).create(GameApiService::class.java))
        gameViewModel = ViewModelProvider(this, GameViewModelFactory(gameRepository)).get(GameViewModel::class.java)
    }

    private fun setupButtonListeners() {
        val buttonHandler = GameViewButtonHandler(this, btnBack)
        buttonHandler.setupButtonListeners()

        btnNext.setOnClickListener {
            checkIfNextable()
            if (nextable) {
                // 다음 단계로 넘어가는 로직 추가
            }
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
                if(resoponse.progress.state==="PLAYING"){
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
            tabLayout.addTab(tabLayout.newTab().setText("점수 입력"))
            tabLayout.addTab(tabLayout.newTab().setText("현재 라운드 보기"))
            tabLayout.addTab(tabLayout.newTab().setText("전체 점수 보기"))
        } else {
            tabLayout.addTab(tabLayout.newTab().setText("현재 라운드 보기"))
            tabLayout.addTab(tabLayout.newTab().setText("전체 점수 보기"))
        }

        if (isHost) {
            replaceFragment(ScoringRoundFragment(), game)
        } else {
            replaceFragment(CurrentRoundFragment(), game)
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
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

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

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
            .replace(R.id.game_view_fragment, fragment)
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
