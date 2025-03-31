package com.bavuchoko.jsparkgolf.ui.game.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.network.RetrofitFactory
import com.bavuchoko.jsparkgolf.repository.ScoreRepository
import com.bavuchoko.jsparkgolf.service.ScoreService
import com.bavuchoko.jsparkgolf.ui.game.view.GameViewActivity
import com.bavuchoko.jsparkgolf.ui.game.view.ScoreHandler
import com.bavuchoko.jsparkgolf.viewmodel.ScoreViewModel
import com.bavuchoko.jsparkgolf.viewmodel.factory.ScoreViewModelFactory
import com.bavuchoko.jsparkgolf.vo.GameVo
import com.bavuchoko.jsparkgolf.vo.ScoreVo
import com.bavuchoko.jsparkgolf.vo.UserVo

class ScoringRoundFragment : Fragment() {

    private lateinit var scoreGrid: GridLayout
    private lateinit var game: GameVo
    private val scoreList = mutableListOf<ScoreVo>()
    private var isPlaying: Boolean = false
    private var nextable: Boolean = false
    private lateinit var btnNext: TextView
    private lateinit var scoreViewModel: ScoreViewModel
    private val scoresForSave: MutableList<ScoreVo> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            game = it.getParcelable("game")!!
            if(game.progress.state =="PLAYING"){
                isPlaying = true
            }
        }
        val scoreRepository = ScoreRepository(RetrofitFactory.create(requireContext()).create(ScoreService::class.java))
        val view = inflater.inflate(R.layout.fragment_view_game_scoring, container, false)
        scoreViewModel = ViewModelProvider(this, ScoreViewModelFactory(scoreRepository)).get(ScoreViewModel::class.java)
        btnNext = view.findViewById(R.id.btn_next)
        scoreGrid = view.findViewById(R.id.scoreGrid)
        val playerCount = game.players.size
        btnNext.setOnClickListener {
            if (nextable) {
                updateGameProgress()
            }else{

            }
        }
        for (i in 0 until playerCount) {
            val scoreView = ScoreHandler(requireContext())
            scoreView.setPlayerName(game.players[i].name)
            // ⚡ GridLayout.LayoutParams 설정
            val params = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }



            scoreView.layoutParams = params
            scoreView.setOnScoreChangeListener { newScore ->
                val scoreVo = ScoreVo(
                    gameId = game.id,
                    playerId = game.players[i].id,
                    turn = 1,
                    half = 1,
                    hole = 1,
                    hit = newScore
                )
                updateScoreList(scoreVo)

                onScoreUpdated(scoreList)
            }

            scoreGrid.addView(scoreView)
        }
        return view
    }

    private fun updateScoreList(score: ScoreVo) {
        val existingIndex = scoreList.indexOfFirst {
            it.playerId == score.playerId && it.hole == score.hole
        }

        if (existingIndex != -1) {
            scoreList[existingIndex] = score
        } else {
            scoreList.add(score)
        }
    }

    fun onScoreUpdated(scoreList: List<ScoreVo>) {
        scoresForSave.clear()
        scoresForSave.addAll(scoreList)
        Log.d("GameViewActivity", "Scores for save: $scoresForSave")

        checkIfNextable()
    }


    fun getScoreList(): List<ScoreVo> {
        return scoreList
    }

    private fun updateGameProgress() {
        if (isPlaying) {
            scoreViewModel.saveScores(game.id, scoresForSave)
        } else {
            Log.d("GameViewActivity", "게임이 진행 중이 아닙니다.")
        }
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
