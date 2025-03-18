package com.bavuchoko.jsparkgolf.ui.game.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bavuchoko.jsparkgolf.R

class ScoreHandler  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : CardView(context, attrs) {

    private var score = 0
    private val scoreText: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.score_input_handler, this, true)
        cardElevation = 0f

        scoreText = findViewById(R.id.scoreText)
        val upperTouch = findViewById<View>(R.id.upperTouch)
        val lowerTouch = findViewById<View>(R.id.lowerTouch)

        upperTouch.setOnClickListener { updateScore(1) }
        lowerTouch.setOnClickListener { updateScore(-1) }
        updateUI()
    }

    private fun updateScore(delta: Int) {
        score += delta
        updateUI()
    }

    private fun updateUI() {
        scoreText.text = score.toString()
    }
}