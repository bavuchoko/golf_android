package com.bavuchoko.jsparkgolf.component.game.view

import android.content.Context
import android.widget.Button

class GameViewButtonHandler(
    private val context: Context,
    private val btnBack: Button,
) {

    fun setupButtonListeners() {

        btnBack.setOnClickListener {
            if (context is android.app.Activity) {
                context.finish()
            }
        }

    }
}