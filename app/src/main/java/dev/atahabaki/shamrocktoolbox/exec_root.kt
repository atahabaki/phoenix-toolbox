package dev.atahabaki.shamrocktoolbox

import android.util.Log

fun execRoot(command: String,tag: String) {
    try {
        java.lang.Runtime.getRuntime().exec("su -c $command")
    } catch (e: Exception) {
        Log.d(tag,"Exception thrown... ${e.message}")
    }
}