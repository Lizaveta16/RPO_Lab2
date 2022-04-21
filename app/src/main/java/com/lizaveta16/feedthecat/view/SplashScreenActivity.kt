package com.lizaveta16.feedthecat.view


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.lizaveta16.feedthecat.R
import com.lizaveta16.feedthecat.databinding.ActivitySplashBinding

class SplashScreenActivity : AppCompatActivity() {

    lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val animationFadeIn = AnimationUtils.loadAnimation(this@SplashScreenActivity, R.anim.dev_animation)
        binding.devText.startAnimation(animationFadeIn)

            binding.logo.alpha = 0f
            binding.logo.animate().setDuration(1500).alpha(1f).withEndAction{
                val i = Intent(this, SignInActivity::class.java)
                startActivity(i)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
    }
}