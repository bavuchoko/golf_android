package com.bavuchoko.jsparkgolf.component.main.game

import android.content.Context
import android.widget.Button
import androidx.core.content.ContextCompat
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.viewmodel.GameViewModel

class GameSearchButtonHandler(
    private val context: Context,
    private val gameViewModel: GameViewModel,
    private val btnSearchOpen: Button,
    private val btnSearchPlaying: Button,
    private val btnSearchClose: Button,
    private val btnSearchPlayer: Button
) {
    fun setupButtonListeners() {
        btnSearchOpen.setOnClickListener {
            gameViewModel.status = "OPEN"
            updateButtonStyles()
            gameViewModel.getList(0, 10)
        }

        btnSearchPlaying.setOnClickListener {
            gameViewModel.status = "PLAYING"
            updateButtonStyles()
            gameViewModel.getList(0, 10)
        }

        btnSearchClose.setOnClickListener {
            gameViewModel.status = "CLOSED"
            updateButtonStyles()
            gameViewModel.getList(0, 10)
        }

        btnSearchPlayer.setOnClickListener {
            gameViewModel.player = !gameViewModel.player
            updateButtonStyles()
            gameViewModel.getList(0, 10)
        }
    }

    private fun updateButtonStyles() {
        val selectedBg = ContextCompat.getDrawable(context, R.color.app_color)
        val defaultBg = ContextCompat.getDrawable(context, R.drawable.btn_un_selected_basic)
        val selectedTextColor = ContextCompat.getColor(context, android.R.color.white)
        val defaultTextColor = ContextCompat.getColor(context, R.color.dee_gray)

        btnSearchOpen.background = if (gameViewModel.status == "OPEN") selectedBg else defaultBg
        btnSearchOpen.setTextColor(if (gameViewModel.status == "OPEN") selectedTextColor else defaultTextColor)
        btnSearchPlaying.background = if (gameViewModel.status == "PLAYING") selectedBg else defaultBg
        btnSearchPlaying.setTextColor(if (gameViewModel.status == "PLAYING") selectedTextColor else defaultTextColor)
        btnSearchClose.background = if (gameViewModel.status == "CLOSED") selectedBg else defaultBg
        btnSearchClose.setTextColor(if (gameViewModel.status == "CLOSED") selectedTextColor else defaultTextColor)
        btnSearchPlayer.background = if (gameViewModel.player) selectedBg else defaultBg
        btnSearchPlayer.setTextColor(if (gameViewModel.player) selectedTextColor else defaultTextColor)
    }
}