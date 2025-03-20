package com.bavuchoko.jsparkgolf.ui.game.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.bavuchoko.jsparkgolf.R

class ScoreHandler  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : CardView(context, attrs) {

    private var score = 0
    private var playerName: TextView
    private val scoreText: TextView
    private val avgText: TextView
    private val totText: TextView
    private var onScoreChangeListener: ((Int) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.score_input_handler, this, true)
        cardElevation = 0f

        scoreText = findViewById(R.id.scoreText)
        avgText = findViewById(R.id.avgText)
        totText = findViewById(R.id.totText)
        playerName = findViewById(R.id.playerName)

        val upperTouch = findViewById<View>(R.id.upperTouch)
        val lowerTouch = findViewById<View>(R.id.lowerTouch)

        upperTouch.setOnClickListener { updateScore(1) }
        lowerTouch.setOnClickListener { updateScore(-1) }
        updateUI()
    }

    fun setPlayerName(name: String) {
        playerName.text = name
    }

    fun setOnScoreChangeListener(listener: (Int) -> Unit) {
        onScoreChangeListener = listener
    }

    private fun updateScore(delta: Int) {
        val newScore = score + delta
        if (newScore >= 0) {
            score = newScore
            updateUI()
            onScoreChangeListener?.invoke(score)
        }else{
            val inflater = LayoutInflater.from(context)
            val layout = inflater.inflate(R.layout.custom_toast, null)

            val text: TextView = layout.findViewById(R.id.toastText)
            text.text = "점수는 0보다 작을 수 없습니다."

            val toast = Toast(context)
            toast.duration = Toast.LENGTH_SHORT
            toast.setGravity(Gravity.TOP, 0, 0)
            toast.view = layout
            toast.show()
        }
    }


    private fun updateUI() {
        scoreText.text = score.toString()
    }
}