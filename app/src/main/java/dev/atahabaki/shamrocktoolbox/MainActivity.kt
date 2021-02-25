package dev.atahabaki.shamrocktoolbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun execRoot(command: String) {
        try {
            java.lang.Runtime.getRuntime().exec("su -c $command")
        } catch (e: Exception) {
            Log.d("${applicationContext.packageName}.ExecRoot","Exception thrown... ${e.message}")
        }
    }
}