package com.example.controldealmacen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.controldealmacen.ui.login.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.elorrieta)
        val texto = findViewById<TextView>(R.id.texto_corporativo)

        logo.clearAnimation()
        texto.clearAnimation()

        val animacionLogo = AnimationUtils.loadAnimation(this, R.anim.splash_anim)
        val animacionTexto = AnimationUtils.loadAnimation(this, R.anim.text_anim)

        logo.startAnimation(animacionLogo)
        texto.startAnimation(animacionTexto)

        // Esperar 4 segundos para que de tiempo a ver el efecto completo
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            finish()
        }, 4000)
    }
}