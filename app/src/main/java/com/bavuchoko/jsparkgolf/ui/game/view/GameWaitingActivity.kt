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

class GameWaitingActivity : AppCompatActivity() {

    private lateinit var btnBack: Button
    private lateinit var gameViewModel: GameViewModel
    private lateinit var btnStart: TextView
    private var isHost: Boolean = false
    private var isOpen: Boolean = false
    private var statable: Boolean = false
    private lateinit var game:GameVo

    private val scoresForSave: MutableList<ScoreVo> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_waiting)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        btnStart = findViewById(R.id.btn_start)

        initializeViews()
        setupButtonListeners()
        val gameId = intent.getLongExtra("gameId", -1L)
        if (gameId != -1L) {
            getGameDetails(gameId)
        }
    }

    private fun initializeViews() {
        btnBack = findViewById(R.id.btn_back)
        val gameRepository = GameRepository(RetrofitFactory.create(this).create(GameApiService::class.java))
        gameViewModel = ViewModelProvider(this, GameViewModelFactory(gameRepository)).get(GameViewModel::class.java)
    }

    private fun setupButtonListeners() {
        val buttonHandler = GameViewButtonHandler(this, btnBack)
        buttonHandler.setupButtonListeners()

        btnStart.setOnClickListener {
            if (statable) {
                gameStart()
            }else{

            }
        }
    }

    private fun gameStart() {
        if (isOpen) {

        } else {
            Log.d("GameViewActivity", "게임이 대기 중이 아닙니다.")
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
                if(resoponse.progress.state =="OPEN"){
                    isOpen = true
                }

            }
        }
    }


}
