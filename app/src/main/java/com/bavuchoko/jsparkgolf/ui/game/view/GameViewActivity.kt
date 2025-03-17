package com.bavuchoko.jsparkgolf.ui.game.view

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.GridLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.component.game.view.GameViewButtonHandler
import com.bavuchoko.jsparkgolf.ui.game.view.fragment.CurrentRoundFragment
import com.bavuchoko.jsparkgolf.ui.game.view.fragment.TotalScoreFragment
import com.bavuchoko.jsparkgolf.vo.GameVo
import com.google.android.material.tabs.TabLayout

class GameViewActivity : AppCompatActivity()  {

    private lateinit var btnBack: Button
    private lateinit var tabLayout: TabLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_view)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val game: GameVo? = intent.getParcelableExtra("game")

        btnBack = findViewById(R.id.btn_back)
        tabLayout = findViewById(R.id.tab_layout)

        val buttonHandler = GameViewButtonHandler(this,btnBack)
        buttonHandler.setupButtonListeners()

        replaceFragment(CurrentRoundFragment())

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    when (it.position) {
                        0 -> {
                            Log.d("GameViewActivity", "CurrentRoundFragment 선택됨")
                            replaceFragment(CurrentRoundFragment())
                        }
                        1 -> {
                            Log.d("GameViewActivity", "TotalScoreFragment 선택됨")
                            replaceFragment(TotalScoreFragment())
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        Log.d("GameViewActivity", "Fragment 교체: ${fragment::class.java.simpleName}")
        supportFragmentManager.beginTransaction()
            .replace(R.id.game_view_fragment, fragment)
            .commit()
    }

}