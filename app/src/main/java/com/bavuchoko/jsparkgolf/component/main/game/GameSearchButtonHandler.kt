package com.bavuchoko.jsparkgolf.component.main.game

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.ui.game.create.GameCreateActivity
import com.bavuchoko.jsparkgolf.ui.game.create.QuickCreateActivity
import com.bavuchoko.jsparkgolf.viewmodel.GameViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GameSearchButtonHandler(
    private val context: Context,
    private val gameViewModel: GameViewModel,
    private val btnSearchOpen: Button,
    private val btnSearchPlaying: Button,
    private val btnSearchClose: Button,
    private val btnSearchPlayer: Button,
    private val fabContainer: LinearLayout,
    private val fabQuick: LinearLayout,
    private val fabCreate: LinearLayout,
    private val fabMain: FloatingActionButton,
    private val fabBackground: View,
    private var isFabOpen: Boolean = false
) {

    init {
        fabContainer.post {
            fabContainer.pivotY = fabContainer.height.toFloat() // 바닥 기준
            fabContainer.scaleY = 0f // 처음에는 축소 상태
            fabContainer.translationY = 50f // fab_main과 간격 유지
            fabContainer.visibility = View.GONE // 기본적으로 숨김
            fabMain.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.app_color))
            fabMain.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
        }
    }

    fun setupButtonListeners() {
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

        fabCreate.setOnClickListener {
            closeFabMenu()
            val intent = Intent(context, GameCreateActivity::class.java)
            context.startActivity(intent)
        }

        fabQuick.setOnClickListener {
            closeFabMenu()
            val intent = Intent(context, QuickCreateActivity::class.java)
            context.startActivity(intent)
        }

        fabMain.setOnClickListener {
            if (isFabOpen) {
                closeFabMenu()
            } else {
                openFabMenu()
            }
        }
        fabBackground.setOnClickListener {
                closeFabMenu()
        }
    }

    fun updateButtonStyles() {
        val selectedBg = ContextCompat.getDrawable(context, R.color.app_color)
        val defaultBg = ContextCompat.getDrawable(context, R.drawable.btn_un_selected_basic)
        val selectedTextColor = ContextCompat.getColor(context, android.R.color.white)
        val defaultTextColor = ContextCompat.getColor(context, R.color.dee_gray)

        btnSearchOpen.background = if (gameViewModel.status == "OPEN") selectedBg else defaultBg
        btnSearchOpen.setTextColor(if (gameViewModel.status == "OPEN") selectedTextColor else defaultTextColor)
        btnSearchPlaying.background = if (gameViewModel.status == "PLAYING") selectedBg else defaultBg
        btnSearchPlaying.setTextColor(if (gameViewModel.status == "PLAYING") selectedTextColor else defaultTextColor)
        btnSearchClose.background = if (gameViewModel.status == "CLOSED") selectedBg else defaultBg
        btnSearchClose.setTextColor(if (gameViewModel.status == "CLOSED") selectedTextColor else defaultTextColor)
        btnSearchPlayer.background = if (gameViewModel.player) selectedBg else defaultBg
        btnSearchPlayer.setTextColor(if (gameViewModel.player) selectedTextColor else defaultTextColor)
    }

    private fun openFabMenu() {
        isFabOpen = true
        fabContainer.visibility = View.VISIBLE
        fabBackground.visibility = View.VISIBLE
        fabMain.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
        fabMain.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black))
        fabMain.setImageResource(R.drawable.baseline_close_24)
        // PivotY 설정 (바닥을 기준으로 크기 변경)
        fabContainer.pivotY = fabContainer.height.toFloat()

        // Scale Y 애니메이션 (바닥 기준으로 커짐)
        val scaleY = ObjectAnimator.ofFloat(fabContainer, "scaleY", 0f, 1.1f, 1f).apply {
            duration = 250
            interpolator = AccelerateDecelerateInterpolator()
        }

        // Y축 이동 (위로 올라감)
        val moveUp = ObjectAnimator.ofFloat(fabContainer, "translationY", 20f, 0f).apply {
            duration = 250
            interpolator = AccelerateDecelerateInterpolator()
        }

        // 애니메이션 실행
        AnimatorSet().apply {
            playTogether(scaleY, moveUp)
            start()
        }
    }



    private fun closeFabMenu() {
        isFabOpen = false
        fabBackground.visibility = View.GONE
        fabMain.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.app_color))
        fabMain.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
        fabMain.setImageResource(R.drawable.baseline_add_24)
// Scale animation (Y축)
        val scaleY = ObjectAnimator.ofFloat(fabContainer, "scaleY", 1f, 1.1f, 0f).apply {
            duration = 250
            interpolator = AccelerateDecelerateInterpolator()
        }

        // Y축 이동 (아래로 내려가기)
        val moveDown = ObjectAnimator.ofFloat(fabContainer, "translationY", 0f, 20f).apply {
            duration = 250
            interpolator = AccelerateDecelerateInterpolator()
        }

        // 애니메이션 조합 실행 후 뷰 숨김 처리
        AnimatorSet().apply {
            playTogether(scaleY, moveDown)
            start()
        }

        // 애니메이션 끝난 후 뷰 숨기기
        fabContainer.postDelayed({ fabContainer.visibility = View.GONE }, 250)
    }
}