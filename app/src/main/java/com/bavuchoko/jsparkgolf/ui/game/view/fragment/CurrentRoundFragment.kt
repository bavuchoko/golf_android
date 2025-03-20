package com.bavuchoko.jsparkgolf.ui.game.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.network.ScoreWebSocketListener
import com.bavuchoko.jsparkgolf.network.WebSocketClient
import com.bavuchoko.jsparkgolf.vo.GameVo
import com.bavuchoko.jsparkgolf.vo.ScoreVo
import okhttp3.WebSocketListener

class CurrentRoundFragment : Fragment() {

    private lateinit var scoreGrid: GridLayout
    private lateinit var webSocketClient: WebSocketClient
    private lateinit var listener: WebSocketListener
    private lateinit var game: GameVo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_view_game_current, container, false)
        arguments?.let {
            game = it.getParcelable("game")!!
        }
        val scoreUpdateCallback: (List<ScoreVo>) -> Unit = { scores ->
            // UI 업데이트 로직
            updateScoreOnUI(scores)
        }

        listener = ScoreWebSocketListener(scoreUpdateCallback)
        return view
    }



    override fun onStart() {
        Log.d("awdawd","awd")
        super.onStart()
        webSocketClient = WebSocketClient(game.id, listener)
        webSocketClient.connect()
    }

    override fun onStop() {
        super.onStop()
        webSocketClient.close()
    }


    private fun updateScoreOnUI(scores: List<ScoreVo>) {
        // UI 갱신 로직: 예시로 점수들을 GridLayout에 반영
//        scoreGrid.removeAllViews()
//
//        for (score in scores) {
//            val textView = TextView(requireContext())
//            textView.text = "Player: ${score.playerName}, Score: ${score.score}"
//            scoreGrid.addView(textView)
//        }
    }

}