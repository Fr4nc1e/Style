package com.example.style.animation

import android.view.View
import android.view.animation.TranslateAnimation
import androidx.viewbinding.ViewBinding

object SetAnimation {

    private lateinit var animation: TranslateAnimation

    fun setAnimation(binding: ViewBinding, view1: View, view2: View) {
        view1.animate().alpha(0f).duration = 1

        animation = TranslateAnimation(
            0F,
            0F,
            0F,
            -1000F
        ).apply {
            duration = 1000
            fillAfter = false
            setAnimationListener(MyAnimationListener(binding))
        }

        view2.animation = animation
    }
}
