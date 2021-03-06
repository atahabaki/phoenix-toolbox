package dev.atahabaki.shamrocktoolbox

import android.util.Log

fun execRoot(command: String,tag: String) {
    try {
        val p = java.lang.Runtime.getRuntime().exec("su -c $command")
        p.waitFor()
    } catch (e: Exception) {
        Log.d(tag,"Exception thrown... ${e.message}")
    }
}