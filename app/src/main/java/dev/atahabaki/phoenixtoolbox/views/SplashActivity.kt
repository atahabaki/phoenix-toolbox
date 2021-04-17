package dev.atahabaki.phoenixtoolbox.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Intent(this, HomeActivity::class.java).let {
            startActivity(it)
        }
        finish()
    }
}