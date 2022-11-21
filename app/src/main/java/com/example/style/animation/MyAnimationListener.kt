package com.example.style.animation

import android.view.View
import android.view.animation.Animation
import androidx.viewbinding.ViewBinding
import com.example.style.databinding.ActivityStartBinding

class MyAnimationListener(private val binding: ViewBinding) : Animation.AnimationListener {

    override fun onAnimationStart(animation: Animation?) {
    }

    override fun onAnimationRepeat(animation: Animation?) {
    }

    override fun onAnimationEnd(animation: Animation?) {
        if (binding is ActivityStartBinding) {
            binding.iconImage.clearAnimation()
            binding.iconImage.visibility = View.INVISIBLE
            binding.linearLayout.animate().alpha(1f).duration = 1000
        }
    }
}
