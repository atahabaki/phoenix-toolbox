package dev.atahabaki.shamrocktoolbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import dev.atahabaki.shamrocktoolbox.databinding.ActivityHomeBinding
import java.lang.Exception

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    fun execRoot(command: String) {
        try {
            java.lang.Runtime.getRuntime().exec("su -c $command")
        } catch (e: Exception) {
            Log.d("${applicationContext.packageName}.ExecRoot","Exception thrown... ${e.message}")
        }
    }
}