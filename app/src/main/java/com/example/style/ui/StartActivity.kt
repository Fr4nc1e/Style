package com.example.style.ui

import android.content.Intent
import android.os.Bundle
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import com.example.style.MainActivity
import com.example.style.animation.MyAnimationListener
import com.example.style.databinding.ActivityStartBinding
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding
    private lateinit var animation: TranslateAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAnimation()

        binding.login.setOnClickListener {
            startActivity(
                Intent(this, LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
        binding.register.setOnClickListener {
            startActivity(
                Intent(this, RegisterActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
    }

    override fun onStart() {
        super.onStart()

        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun setAnimation() {
        binding.linearLayout.animate().alpha(0f).duration = 1

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

        binding.iconImage.animation = animation
    }
}
