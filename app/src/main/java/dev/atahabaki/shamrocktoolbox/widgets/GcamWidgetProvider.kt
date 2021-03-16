package dev.atahabaki.shamrocktoolbox.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.widget.RemoteViews
import dev.atahabaki.shamrocktoolbox.R
import dev.atahabaki.shamrocktoolbox.execRoot
import java.io.BufferedReader
import java.io.InputStreamReader

class GcamWidgetProvider : AppWidgetProvider() {

    private val gcamProp = "persist.camera.HAL3.enabled"
    private val REFRESH_ACTION = "android.appwidget.action.APPWIDGET_UPDATE"

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        appWidgetIds?.forEach {
            val pendingIntent: PendingIntent = Intent(context, GcamWidgetProvider::class.java).let {
                PendingIntent.getActivity(context, 0, it, 0)
            }
            val views: RemoteViews = RemoteViews(
                context?.packageName,
                R.layout.widget_gcam
            ).apply {
                setOnClickPendingIntent(R.id.gcam_widget_status_changer, pendingIntent)
                if (getGcamStatus(context)) {
                    setTextViewText(R.id.gcam_widget_status_changer, context?.getString(R.string.gcam_on))
                } else setTextViewText(R.id.gcam_widget_status_changer, context?.getString(R.string.gcam_off))
            }
            appWidgetManager?.updateAppWidget(it,views)
        }
    }

    private fun selfPendingIntent(context: Context?) : PendingIntent {
        val intent: Intent = Intent(context, GcamWidgetProvider::class.java)
        intent.action = REFRESH_ACTION
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun getGcamStatus(context: Context?): Boolean {
        try {
            val p = java.lang.Runtime.getRuntime().exec("getprop $gcamProp")
            p.waitFor()
            val stdOut = BufferedReader(InputStreamReader(p.inputStream))
            val line = stdOut.readLine().trim()
            return line == "1"
        } catch (e: Exception) {
            Log.d("${context?.packageName}.toggleGcam", "${e.message}")
        }
        return false
    }

    private fun disableGcam(context: Context?) {
        execRoot("setprop $gcamProp 0", "${context?.packageName}.setProp")
    }

    private fun enableGcam(context: Context?) {
        execRoot("setprop $gcamProp 1", "${context?.packageName}.setProp")
    }
}