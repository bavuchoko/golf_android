package com.bavuchoko.jsparkgolf.ui.game

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
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
import com.bavuchoko.jsparkgolf.network.RetrofitFactory
import com.bavuchoko.jsparkgolf.repository.GameRepository
import com.bavuchoko.jsparkgolf.service.GameApiService
import com.bavuchoko.jsparkgolf.ui.MyPlaceSettingActivity
import com.bavuchoko.jsparkgolf.viewmodel.GameViewModel
import com.bavuchoko.jsparkgolf.viewmodel.factory.GameViewModelFactory


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
    private lateinit var arrowUp: ImageView

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

        btnMyPlace = view.findViewById(R.id.btn_my_place)
        btnMyPlaceSetting = view.findViewById(R.id.setting_my_place)


        btnSearchOpen = view.findViewById(R.id.btn_search_open)
        btnSearchPlaying = view.findViewById(R.id.btn_search_playing)
        btnSearchClose = view.findViewById(R.id.btn_search_close)
        btnSearchPlayer = view.findViewById(R.id.btn_search_player)
        arrowUp = view.findViewById(R.id.arrow_up);

        gameViewModel = ViewModelProvider(this, GameViewModelFactory(gameRepository)).get(GameViewModel::class.java)

        // 버튼 클릭 리스너 추가
        
        //지역설정 버튼
//        btnMyPlaceSetting.setOnClickListener {
//            val transaction = requireActivity().supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.content_container, MyPlaceSettingFragment())
//            transaction.addToBackStack(null)
//            transaction.commit()
//        }
        btnMyPlaceSetting.setOnClickListener {
            val intent = Intent(requireContext(), MyPlaceSettingActivity::class.java)
            startActivity(intent)
        }


        //대기중 버튼
        btnSearchOpen.setOnClickListener {
            gameViewModel.status = "OPEN"
            updateButtonStyles()
            gameViewModel.getList(0, 10)
        }
        //진행중 버튼
        btnSearchPlaying.setOnClickListener {
            gameViewModel.status = "PLAYING"
            updateButtonStyles()
            gameViewModel.getList(0, 10)
        }
        //종료됨 버튼
        btnSearchClose.setOnClickListener {
            gameViewModel.status = "CLOSED"
            updateButtonStyles()
            gameViewModel.getList(0, 10)
        }
        //내 경기 버튼
        btnSearchPlayer.setOnClickListener {
            gameViewModel.player = !gameViewModel.player
            updateButtonStyles()
            gameViewModel.getList(0, 10)
        }

        val jwtCity: String? = CommonMethod.getStoredValue(requireContext(), "city");
        var city: String? = jwtCity
        gameViewModel.city = city
        var page: Int = 0
        var size: Int = 10

        updateButtonStyles()
        updateMyPlaceButton(city)



        val recyclerView: RecyclerView = view.findViewById(R.id.data_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        topBar = view.findViewById(R.id.game_top_bar)
        nestedScrollView = view.findViewById(R.id.game_nested_container)
        noDataNotice = view.findViewById(R.id.no_data_notice)
        nullMyPlace = view.findViewById(R.id.notice_my_place_null)
        nestedScrollView = view.findViewById(R.id.game_nested_container)


        if(!city.isNullOrBlank()) {
            nullMyPlace.visibility= View.GONE
            gameViewModel.getList(page, size)
        }else{
            nestedScrollView.visibility= View.GONE
            noDataNotice.visibility= View.GONE
            nullMyPlace.visibility= View.VISIBLE
            arrowUpAnim();
        }




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
}
