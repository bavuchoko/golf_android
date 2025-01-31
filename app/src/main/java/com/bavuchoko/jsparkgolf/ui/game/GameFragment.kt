package com.bavuchoko.jsparkgolf.ui.game

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.adpater.GameRecyclerAdapter
import com.bavuchoko.jsparkgolf.common.CommonMethod
import com.bavuchoko.jsparkgolf.network.RetrofitFactory
import com.bavuchoko.jsparkgolf.repository.GameRepository
import com.bavuchoko.jsparkgolf.service.GameApiService
import com.bavuchoko.jsparkgolf.viewmodel.GameViewModel
import com.bavuchoko.jsparkgolf.viewmodel.factory.GameViewModelFactory


class GameFragment : Fragment() {

    private lateinit var gameAdapter: GameRecyclerAdapter
    private lateinit var topBar: LinearLayout
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var gameViewModel: GameViewModel
    private lateinit var btnSearchOpen: Button
    private lateinit var btnSearchPlaying: Button
    private lateinit var btnSearchClose: Button
    private lateinit var btnSearchPlayer: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // fragment_game 레이아웃을 인플레이트하여 반환
        return inflater.inflate(R.layout.fragment_game, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gameService = RetrofitFactory.create(requireContext()).create(GameApiService::class.java)
        val gameRepository = GameRepository(gameService);

        btnSearchOpen = view.findViewById(R.id.btn_search_open)
        btnSearchPlaying = view.findViewById(R.id.btn_search_playing)
        btnSearchClose = view.findViewById(R.id.btn_search_close)
        btnSearchPlayer = view.findViewById(R.id.btn_search_player)

        gameViewModel = ViewModelProvider(this, GameViewModelFactory(gameRepository)).get(GameViewModel::class.java)

        updateButtonStyles()

        // 버튼 클릭 리스너 추가
        btnSearchOpen.setOnClickListener {
            gameViewModel.status = "OPEN"
            updateButtonStyles()
            gameViewModel.getList(0, 10)
        }

        btnSearchPlaying.setOnClickListener {
            gameViewModel.status = "PLAYING"
            updateButtonStyles()
            gameViewModel.getList(0, 10)
        }

        btnSearchClose.setOnClickListener {
            gameViewModel.status = "CLOSED"
            updateButtonStyles()
            gameViewModel.getList(0, 10)
        }

        btnSearchPlayer.setOnClickListener {
            gameViewModel.player = !gameViewModel.player
            updateButtonStyles()
            gameViewModel.getList(0, 10)
        }

        val jwtCity: String? = CommonMethod.getStoredValue(requireContext(), "city");
        var city: String = jwtCity ?: "세종특별시"
        gameViewModel.city = city
        var page: Int = 0
        var size: Int = 10
        var selection: MutableList<String>
            = if(jwtCity != null)
                mutableListOf(city, "내 지역 설정")
            else  mutableListOf("내 지역")


        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, selection)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val recyclerView: RecyclerView = view.findViewById(R.id.data_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val locationSpinner: Spinner = view.findViewById(R.id.city_spinner)
        locationSpinner.adapter = spinnerAdapter

        topBar = view.findViewById(R.id.game_top_bar)
        nestedScrollView = view.findViewById(R.id.game_nested_container)


        gameViewModel.getList(page, size)





        gameViewModel.gameList.observe(viewLifecycleOwner){ gameList ->
            if(gameList != null){
                val groupedGames = gameList.groupBy { it.playDate.substring(0,10) }

                gameAdapter = GameRecyclerAdapter(groupedGames, object : GameRecyclerAdapter.OnItemClickListener {
                    override fun onItemClick(url: String) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    }
                })
                recyclerView.adapter = gameAdapter
            }
        }



        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > 50) {
                topBar.setBackgroundResource(R.drawable.border_bottom) // 경계선 추가
            } else {
                topBar.setBackgroundColor(Color.TRANSPARENT) // 경계선 제거
            }
        })
    }
    // 버튼 상태에 따라 스타일 변경
    private fun updateButtonStyles() {
        val selectedBg = ContextCompat.getDrawable(requireContext(), R.drawable.btn_selected_basic)
        val defaultBg = ContextCompat.getDrawable(requireContext(), R.drawable.btn_un_selected_basic)
        val selectedTextColor = ContextCompat.getColor(requireContext(), android.R.color.white)
        val defaultTextColor = ContextCompat.getColor(requireContext(), R.color.dee_gray)

        btnSearchOpen.background = if (gameViewModel.status == "OPEN") selectedBg else defaultBg
        btnSearchOpen.setTextColor(if (gameViewModel.status == "OPEN") selectedTextColor else defaultTextColor)
        btnSearchPlaying.background = if (gameViewModel.status == "PLAYING") selectedBg else defaultBg
        btnSearchPlaying.setTextColor(if (gameViewModel.status == "PLAYING") selectedTextColor else defaultTextColor)
        btnSearchClose.background = if (gameViewModel.status == "CLOSED") selectedBg else defaultBg
        btnSearchClose.setTextColor(if (gameViewModel.status == "CLOSED") selectedTextColor else defaultTextColor)
        btnSearchPlayer.background = if (gameViewModel.player) selectedBg else defaultBg
        btnSearchPlayer.setTextColor(if (gameViewModel.player) selectedTextColor else defaultTextColor)
    }
}
