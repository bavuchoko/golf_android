package com.bavuchoko.jsparkgolf.ui.game.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.ui.game.view.GameViewActivity
import com.bavuchoko.jsparkgolf.ui.game.view.ScoreHandler
import com.bavuchoko.jsparkgolf.vo.GameVo
import com.bavuchoko.jsparkgolf.vo.ScoreVo
import com.bavuchoko.jsparkgolf.vo.UserVo

class ScoringRoundFragment : Fragment() {

    private lateinit var scoreGrid: GridLayout
    private lateinit var game: GameVo
    private val scoreList = mutableListOf<ScoreVo>()
    private var scoreUpdateListener: ScoreUpdateListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            game = it.getParcelable("game")!!
        }
        val view = inflater.inflate(R.layout.fragment_view_game_scoring, container, false)

        scoreGrid = view.findViewById(R.id.scoreGrid)
        val playerCount = game.players.size

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

                scoreUpdateListener?.onScoreUpdated(scoreList)
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

    fun setScoreUpdateListener(listener: ScoreUpdateListener) {
        scoreUpdateListener = listener
    }

    interface ScoreUpdateListener {
        fun onScoreUpdated(scoreList: List<ScoreVo>)
    }

    fun getScoreList(): List<ScoreVo> {
        return scoreList
    }
}
