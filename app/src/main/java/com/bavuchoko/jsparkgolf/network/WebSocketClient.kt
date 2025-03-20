package com.bavuchoko.jsparkgolf.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketClient(private val gameId: Long, private val listener: WebSocketListener) {

    private var webSocket: WebSocket? = null

    fun connect() {
        val client = OkHttpClient()
        val uri = "wss://todoro.co.kr/score/ws/$gameId"
        val request = Request.Builder().url(uri).build()

        webSocket = client.newWebSocket(request, listener)
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    fun close() {
        webSocket?.close(1000, "Closing WebSocket connection")
    }
}
