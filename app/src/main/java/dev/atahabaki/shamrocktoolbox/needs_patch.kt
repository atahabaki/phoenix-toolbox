package dev.atahabaki.shamrocktoolbox

fun needsPatch(): Boolean {
    listOf<String>("shamrock", "mido").forEach {
        return android.os.Build.DEVICE == it
    }
    return false
}

