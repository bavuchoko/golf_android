package com.bavuchoko.jsparkgolf.network

import com.bavuchoko.jsparkgolf.vo.ScoreVo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class ScoreWebSocketListener(
    private val onScoreUpdate: (List<ScoreVo>) -> Unit // 점수 업데이트 콜백
) : WebSocketListener() {

    override fun onMessage(webSocket: WebSocket, text: String) {
        // 서버로부터 점수 데이터를 받으면 처리
        val scores: List<ScoreVo> = Gson().fromJson(text, object : TypeToken<List<ScoreVo>>() {}.type)

        // 점수 업데이트 콜백 호출 (UI에서 점수 갱신)
        onScoreUpdate(scores)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        // 실패 시 처리 로직 (예: 로그 기록, 재시도 등)
    }
}
