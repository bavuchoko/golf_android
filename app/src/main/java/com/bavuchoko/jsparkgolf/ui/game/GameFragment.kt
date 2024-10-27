package com.bavuchoko.jsparkgolf.ui.game

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.dto.response.GameResponseDto
import com.bavuchoko.jsparkgolf.ui.game.adapter.GameRecyclerAdapter

class GameFragment : Fragment() {

    private lateinit var gameAdapter: GameRecyclerAdapter
    private lateinit var topBar: LinearLayout
    private lateinit var nestedScrollView: NestedScrollView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_game 레이아웃을 인플레이트하여 반환
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        topBar = view.findViewById(R.id.game_top_bar)
        nestedScrollView = view.findViewById(R.id.game_nested_container)
        // 게임 리스트 데이터 생성
        val gameList = arrayListOf(
            GameResponseDto(id = 1, name = "Game 1", description = "Description 1", url = "http://example.com/1"),
            GameResponseDto(id = 2, name = "Game 2", description = "Description 2", url = "http://example.com/2"),
            GameResponseDto(id = 3, name = "Game 2", description = "Description 2", url = "http://example.com/2"),
            GameResponseDto(id = 4, name = "Game 2", description = "Description 2", url = "http://example.com/2"),
            GameResponseDto(id = 5, name = "Game 2", description = "Description 2", url = "http://example.com/2"),
            GameResponseDto(id = 6, name = "Game 2", description = "Description 2", url = "http://example.com/2"),
            GameResponseDto(id = 7, name = "Game 2", description = "Description 2", url = "http://example.com/2"),
            GameResponseDto(id = 8, name = "Game 2", description = "Description 2", url = "http://example.com/2"),
            GameResponseDto(id = 9, name = "Game 2", description = "Description 2", url = "http://example.com/2"),
            GameResponseDto(id = 10, name = "Game 2", description = "Description 2", url = "http://example.com/2"),
            GameResponseDto(id = 11, name = "Game 2", description = "Description 2", url = "http://example.com/2"),
            GameResponseDto(id = 12, name = "Game 2", description = "Description 2", url = "http://example.com/2")
            // 추가 게임 데이터
        )

        // 어댑터 초기화
        gameAdapter = GameRecyclerAdapter(gameList, object : GameRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(url: String) {
                // URL 클릭 시 동작 정의
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        })

        // RecyclerView 설정
        val recyclerView: RecyclerView = view.findViewById(R.id.data_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = gameAdapter

        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > 50) {
                topBar.setBackgroundResource(R.drawable.border_bottom) // 경계선 추가
            } else {
                topBar.setBackgroundColor(Color.TRANSPARENT) // 경계선 제거
            }
        })
    }
}
