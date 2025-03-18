package com.bavuchoko.jsparkgolf.ui.game.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.ui.game.view.ScoreHandler

class ScoringRoundFragment : Fragment() {

    private lateinit var scoreGrid: GridLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_view_game_scoring, container, false)

        scoreGrid = view.findViewById(R.id.scoreGrid)
        val playerCount = 3 // 최대 4명까지 가능

        for (i in 0 until playerCount) {
            val scoreView = ScoreHandler(requireContext())

            // ⚡ GridLayout.LayoutParams 설정
            val params = GridLayout.LayoutParams().apply {
                width = 0 // 셀의 가로 크기를 균등하게 설정
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f) // 균등 분배
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f) // 필요하면 설정
            }

            scoreView.layoutParams = params
            scoreGrid.addView(scoreView)
        }
        return view
    }
}