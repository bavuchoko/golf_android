package com.bavuchoko.jsparkgolf.ui.main.game

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.adpater.GameRecyclerAdapter
import com.bavuchoko.jsparkgolf.common.CommonMethod
import com.bavuchoko.jsparkgolf.component.main.game.GameAnimator
import com.bavuchoko.jsparkgolf.network.RetrofitFactory
import com.bavuchoko.jsparkgolf.repository.GameRepository
import com.bavuchoko.jsparkgolf.repository.UserRepository
import com.bavuchoko.jsparkgolf.service.GameApiService
import com.bavuchoko.jsparkgolf.service.UserApiService
import com.bavuchoko.jsparkgolf.ui.region.MyPlaceSettingActivity
import com.bavuchoko.jsparkgolf.component.main.game.GameSearchButtonHandler
import com.bavuchoko.jsparkgolf.viewmodel.GameViewModel
import com.bavuchoko.jsparkgolf.viewmodel.UserViewModel
import com.bavuchoko.jsparkgolf.viewmodel.factory.GameViewModelFactory
import com.bavuchoko.jsparkgolf.viewmodel.factory.UserViewModelFactory


class GameFragment : Fragment() {

    private lateinit var gameAdapter: GameRecyclerAdapter
    private lateinit var topBar: LinearLayout
    private lateinit var nullMyPlace: FrameLayout
    private lateinit var noDataNotice: LinearLayout
    private lateinit var nestedScrollView: NestedScrollView

    private lateinit var gameViewModel: GameViewModel
    private lateinit var btnMyPlace: Button
    private lateinit var btnMyPlaceSetting: Button
    private lateinit var btnSearchOpen: Button
    private lateinit var btnSearchPlaying: Button
    private lateinit var btnSearchClose: Button
    private lateinit var btnSearchPlayer: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var arrowUp: ImageView
    private var region: String? = null
    private var page: Int = 0
    private var size: Int = 10
    private lateinit var userViewModel: UserViewModel
    private lateinit var gameAnimator: GameAnimator

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
        val userApiService = RetrofitFactory.create(requireContext()).create(UserApiService::class.java)
        val userRepository = UserRepository(userApiService, requireContext())
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository)).get(UserViewModel::class.java)

        btnMyPlace = view.findViewById(R.id.btn_my_place)
        btnMyPlaceSetting = view.findViewById(R.id.setting_my_place)


        btnSearchOpen = view.findViewById(R.id.btn_search_open)
        btnSearchPlaying = view.findViewById(R.id.btn_search_playing)
        btnSearchClose = view.findViewById(R.id.btn_search_close)
        btnSearchPlayer = view.findViewById(R.id.btn_search_player)
        arrowUp = view.findViewById(R.id.arrow_up);

        gameViewModel = ViewModelProvider(this, GameViewModelFactory(gameRepository)).get(GameViewModel::class.java)

        // ArrowAnimator 초기화
        gameAnimator = GameAnimator(arrowUp)

        // 버튼 클릭 리스너 추가
        val buttonHandler = GameSearchButtonHandler(requireContext(), gameViewModel, btnSearchOpen, btnSearchPlaying, btnSearchClose, btnSearchPlayer)
        buttonHandler.setupButtonListeners()


        btnMyPlaceSetting.setOnClickListener {
            val intent = Intent(requireContext(), MyPlaceSettingActivity::class.java)
            startActivity(intent)
        }

        btnMyPlace.setOnClickListener {
            val intent = Intent(requireContext(), MyPlaceSettingActivity::class.java)
            startActivity(intent)
        }


        val jwtCity: String? = CommonMethod.getStoredValue(requireContext(), "region");
        region = jwtCity
        gameViewModel.city = region

        updateButtonStyles()
        updateMyPlaceButton(region)



        recyclerView = view.findViewById(R.id.data_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        topBar = view.findViewById(R.id.game_top_bar)
        nestedScrollView = view.findViewById(R.id.game_nested_container)
        noDataNotice = view.findViewById(R.id.no_data_notice)
        nullMyPlace = view.findViewById(R.id.notice_my_place_null)


        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > 50) {
                topBar.setBackgroundResource(R.drawable.border_bottom) // 경계선 추가
            } else {
                topBar.setBackgroundColor(Color.TRANSPARENT) // 경계선 제거
            }
        })



    }

    private fun setGameListView(recyclerView: RecyclerView) {
        gameViewModel.gameList.observe(viewLifecycleOwner) { gameList ->
            nestedScrollView.visibility=View.VISIBLE
            if (gameList != null) {
                if(gameList.size==0){
                    noDataNotice.visibility=View.VISIBLE
                    recyclerView.visibility=View.GONE
                }else {
                    noDataNotice.visibility=View.GONE
                    recyclerView.visibility=View.VISIBLE
                    val groupedGames = gameList.groupBy { it.playDate.substring(0, 10) }

                    gameAdapter = GameRecyclerAdapter(
                        groupedGames,
                        object : GameRecyclerAdapter.OnItemClickListener {
                            override fun onItemClick(url: String) {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                startActivity(intent)
                            }
                        })
                    recyclerView.adapter = gameAdapter
                }
            }
        }
    }

    private fun viewSwitch(region: String?, page: Int, size: Int) {
        if (!region.isNullOrBlank()) {
            nullMyPlace.visibility = View.GONE
            gameViewModel.getList(page, size)
        } else {
            nestedScrollView.visibility = View.GONE
            noDataNotice.visibility = View.GONE
            nullMyPlace.visibility = View.VISIBLE
            gameAnimator.startAnimation();
        }
    }

    // 버튼 상태에 따라 스타일 변경
    private fun updateButtonStyles() {
        val selectedBg = ContextCompat.getDrawable(requireContext(), R.color.app_color)
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

    private fun updateMyPlaceButton(city:String?){
        if(city==null){
            btnMyPlaceSetting.visibility= View.VISIBLE
            btnMyPlace.visibility= View.GONE
        }else{
            btnMyPlaceSetting.visibility= View.GONE
            btnMyPlace.visibility= View.VISIBLE
            btnMyPlace.text= city
        }
    }




    override fun onResume() {
        super.onResume()
        Log.d("GameFragment", "onResume() 호출됨!")
        val region: String? = CommonMethod.getStoredValue(requireContext(), "region")
        gameViewModel.city=region
        updateMyPlaceButton(region)
        viewSwitch(region, page, size)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        setGameListView(recyclerView)


        Log.d("userViewModel", " ${userViewModel.userRegion.value}")
    }
    override fun onStart() {
        super.onStart()
        Log.d("GameFragment", "onStart() 호출됨!")
    }

    override fun onPause() {
        super.onPause()
        gameAnimator.stopAnimation() // 애니메이션 중지
    }
}
