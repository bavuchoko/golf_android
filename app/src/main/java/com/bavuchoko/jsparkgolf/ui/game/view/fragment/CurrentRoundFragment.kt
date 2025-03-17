package com.bavuchoko.jsparkgolf.ui.game.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.ui.game.view.ScoreHandler

class CurrentRoundFragment : Fragment() {

    private lateinit var scoreGrid: GridLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_view_game_current, container, false)

        scoreGrid = view.findViewById(R.id.scoreGrid)
        val playerCount = 1 // 최대 4명까지 가능

        for (i in 0 until playerCount) {
            val scoreView = ScoreHandler(requireContext())
            scoreGrid.addView(scoreView)
        }
        return view
    }
}