package com.bavuchoko.jsparkgolf.component.game.view

import android.content.Context
import android.content.Intent
import android.widget.Button
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.ui.main.MainActivity

class GameViewButtonHandler(
    private val context: Context,
    private val btnBack: Button,
) {

    fun setupButtonListeners() {

        btnBack.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)

            (context as? android.app.Activity)?.overridePendingTransition(
                R.anim.none,
                R.anim.slide_out_bottom
            )
        }
    }

}