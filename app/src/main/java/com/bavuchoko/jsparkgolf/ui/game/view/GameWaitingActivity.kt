package com.bavuchoko.jsparkgolf.ui.game.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.common.CommonMethod.getValue
import com.bavuchoko.jsparkgolf.component.game.view.GameViewButtonHandler
import com.bavuchoko.jsparkgolf.network.RetrofitFactory
import com.bavuchoko.jsparkgolf.repository.GameRepository
import com.bavuchoko.jsparkgolf.service.GameApiService
import com.bavuchoko.jsparkgolf.viewmodel.GameViewModel
import com.bavuchoko.jsparkgolf.viewmodel.factory.GameViewModelFactory
import com.bavuchoko.jsparkgolf.vo.GameVo
import com.bavuchoko.jsparkgolf.vo.ScoreVo

class GameWaitingActivity : AppCompatActivity() {

    private lateinit var btnBack: Button
    private lateinit var gameViewModel: GameViewModel
    private lateinit var btnStart: TextView
    private var isHost: Boolean = false
    private var isOpen: Boolean = false
    private var startable: Boolean = false
    private lateinit var game:GameVo
    private lateinit var playerGrid: GridLayout

    private val scoresForSave: MutableList<ScoreVo> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_waiting)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        btnStart = findViewById(R.id.btn_start)
        playerGrid = findViewById(R.id.playerGrid)

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
            gameStart()
        }
    }

    private fun gameStart() {
        if(startable){

        }else{

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

            checkStartable()
            checkPlayer()
        }
    }

    private fun checkStartable() {
        startable = false
        if(isHost){
            btnStart.visibility = View.VISIBLE
            if(isOpen){
                btnStart.setTextColor(resources.getColor(R.color.blue))
                startable =true
            }else{
                btnStart.setTextColor(resources.getColor(R.color.gray))
            }
        }else{
            btnStart.visibility = View.GONE
        }
    }

    private fun checkPlayer() {
        playerGrid.removeAllViews() // 기존 뷰 제거
        val maxPlayerCount = 4
        val currentPlayers = game.players

        for (i in 0 until maxPlayerCount) {
            val playerView = if (i < currentPlayers.size) {
                createPlayerView(currentPlayers[i].name) // 참가자 이름 or 이미지
            } else {
                createJoinButton(i) // 참가하기 버튼
            }
            playerGrid.addView(playerView)
        }
    }

    private fun createPlayerView(name: String): View {
        val view = layoutInflater.inflate(R.layout.waiting_player, null)
        return view
    }

    private fun createJoinButton(index: Int): View {
        val button = Button(this).apply {
            text = "참가하기"
            setOnClickListener {
                joinGame()
            }
        }
        return button
    }

    private fun joinGame() {
        val userId = getValue(this, "id")?.toLongOrNull() ?: return

//        gameViewModel.joinGame(game.id, userId).observe(this) { result ->
//            if (result.isSuccessful) {
//                getGameDetails(game.id) // 성공 후 게임 상태 다시 불러오기
//            } else {
//                Toast.makeText(this, "참가 실패: ${result.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

}
