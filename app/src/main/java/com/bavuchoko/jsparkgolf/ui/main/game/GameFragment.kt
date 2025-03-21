package com.bavuchoko.jsparkgolf.ui.main.game

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.adpater.GameRecyclerAdapter
import com.bavuchoko.jsparkgolf.common.CommonMethod
import com.bavuchoko.jsparkgolf.component.main.game.GameSearchButtonHandler
import com.bavuchoko.jsparkgolf.network.RetrofitFactory
import com.bavuchoko.jsparkgolf.repository.GameRepository
import com.bavuchoko.jsparkgolf.repository.UserRepository
import com.bavuchoko.jsparkgolf.service.GameApiService
import com.bavuchoko.jsparkgolf.service.UserApiService
import com.bavuchoko.jsparkgolf.ui.game.view.GameViewActivity
import com.bavuchoko.jsparkgolf.ui.game.view.GameWaitingActivity
import com.bavuchoko.jsparkgolf.ui.region.MyPlaceSettingActivity
import com.bavuchoko.jsparkgolf.viewmodel.GameViewModel
import com.bavuchoko.jsparkgolf.viewmodel.UserViewModel
import com.bavuchoko.jsparkgolf.viewmodel.factory.GameViewModelFactory
import com.bavuchoko.jsparkgolf.viewmodel.factory.UserViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton


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
    private lateinit var fabBackground: View
    private lateinit var fabContainer: LinearLayout
    private lateinit var fabQuick: LinearLayout
    private lateinit var fabCreate: LinearLayout
    private lateinit var fabMain: FloatingActionButton

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
        fabBackground =  view.findViewById(R.id.modal_background)
        fabContainer = view.findViewById(R.id.fab_container)
        fabQuick = view.findViewById(R.id.fab_quick_start)
        fabCreate = view.findViewById(R.id.fab_create_game)
        fabMain = view.findViewById(R.id.fab_main)
        gameViewModel = ViewModelProvider(this, GameViewModelFactory(gameRepository)).get(GameViewModel::class.java)

        // 버튼 핸들러 초기화 및 설정
        val buttonHandler = GameSearchButtonHandler(requireContext(),
            gameViewModel, btnSearchOpen, btnSearchPlaying, btnSearchClose, btnSearchPlayer,
            fabContainer, fabQuick, fabCreate, fabMain, fabBackground
        )
        buttonHandler.setupButtonListeners()
        buttonHandler.updateButtonStyles()
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
                            override fun onItemClick(id: Long, state: String ) {
                                val intent: Intent = when (state) {
                                    "OPEN" -> {
                                        Intent(requireContext(), GameWaitingActivity::class.java).apply {
                                            putExtra("gameId", id)
                                            putExtra("gameState", state)
                                        }
                                    }
                                    "PLAYING" -> {
                                        Intent(requireContext(), GameViewActivity::class.java).apply {
                                            putExtra("gameId", id)
                                            putExtra("gameState", state)
                                        }
                                    }
                                    "CLOSE" -> {
                                        Intent(requireContext(), GameViewActivity::class.java).apply {
                                            putExtra("gameId", id)
                                            putExtra("gameState", state)
                                        }
                                    }
                                    "END" -> {
                                        Intent(requireContext(), GameViewActivity::class.java).apply {
                                            putExtra("gameId", id)
                                            putExtra("gameState", state)
                                        }
                                    }
                                    else -> {
                                        Intent(requireContext(), GameViewActivity::class.java).apply {
                                            putExtra("gameId", id)
                                            putExtra("gameState", state)
                                        }
                                    }
                                }
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
            arrowUpAnim();
        }
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


    private fun arrowUpAnim(){
        val animator = ObjectAnimator.ofFloat(arrowUp, "translationY", 0f, 30f)
        animator.setDuration(500) // 0.5초 동안 이동
        animator.interpolator = LinearInterpolator()
        animator.repeatMode = ValueAnimator.REVERSE // 위-아래 반복
        animator.repeatCount = ValueAnimator.INFINITE // 무한 반복
        animator.start()
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
}
