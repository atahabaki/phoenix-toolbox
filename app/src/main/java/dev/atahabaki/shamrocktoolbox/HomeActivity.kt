package dev.atahabaki.shamrocktoolbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import dev.atahabaki.shamrocktoolbox.databinding.ActivityHomeBinding
import java.lang.Exception

class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    private val binding = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    fun execRoot(command: String) {
        try {
            java.lang.Runtime.getRuntime().exec("su -c $command")
        } catch (e: Exception) {
            Log.d("${applicationContext.packageName}.ExecRoot","Exception thrown... ${e.message}")
        }
    }
}