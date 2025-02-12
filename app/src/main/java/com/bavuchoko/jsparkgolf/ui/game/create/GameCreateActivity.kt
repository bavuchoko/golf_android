package com.bavuchoko.jsparkgolf.ui.game.create

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.component.game.create.GameCreateButtonHandler
import com.bavuchoko.jsparkgolf.component.main.game.GameSearchButtonHandler

class GameCreateActivity : AppCompatActivity()  {

    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_create)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        btnBack = findViewById(R.id.btn_back)

        val buttonHandler = GameCreateButtonHandler(this,btnBack)
        buttonHandler.setupButtonListeners()
    }

}