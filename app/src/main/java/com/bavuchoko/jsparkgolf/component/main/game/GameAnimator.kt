package com.bavuchoko.jsparkgolf.component.main.game

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.LinearInterpolator

class GameAnimator(private val view: View) {
    private var animator: ObjectAnimator? = null

    fun startAnimation() {
        animator = ObjectAnimator.ofFloat(view, "translationY", 0f, 30f).apply {
            duration = 500 // 0.5초 동안 이동
            interpolator = LinearInterpolator()
            repeatMode = ValueAnimator.REVERSE // 위-아래 반복
            repeatCount = ValueAnimator.INFINITE // 무한 반복
            start()
        }
    }

    fun stopAnimation() {
        animator?.cancel()
        animator = null
    }
}