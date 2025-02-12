package com.bavuchoko.jsparkgolf.component.game.create

import android.content.Context
import android.widget.Button
import com.bavuchoko.jsparkgolf.viewmodel.GameViewModel

class GameCreateButtonHandler(
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