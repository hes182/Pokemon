package com.example.pokemon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.example.pokemon.PopUp.LoadingCostume
import com.example.pokemon.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.Runnable

class SplashScreen : AppCompatActivity() {
    private lateinit var bind: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(bind.root)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        Glide.with(this)
            .asGif()
            .fitCenter()
            .load(R.drawable.picacu)
            .into(bind.imgPicagif)

        Handler().postDelayed({
            bind.imgPicagif.visibility = View.GONE
            bind.btnStartpoke.visibility = View.VISIBLE
        }, 2000)

        bind.btnStartpoke.setOnClickListener {
            val inten = Intent(this, MainActivity::class.java)
            startActivity(inten)
            finish()
        }
    }
}