package com.bavuchoko.jsparkgolf.ui.game.view

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.component.game.view.GameViewButtonHandler

class GameViewActivity : AppCompatActivity()  {

    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_view)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        btnBack = findViewById(R.id.btn_back)

        val buttonHandler = GameViewButtonHandler(this,btnBack)
        buttonHandler.setupButtonListeners()
    }

}