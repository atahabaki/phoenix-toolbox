package dev.atahabaki.phoenixtoolbox.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import dev.atahabaki.phoenixtoolbox.R
import dev.atahabaki.phoenixtoolbox.deleteCache

class CleanerWidgetProvider: AppWidgetProvider() {
    companion object {
        const val REFRESH_ACTION = "android.appwidget.action.APPWIDGET_UPDATE"
        const val ACTION_CLEAN = "dev.atahabaki.phoenixtoolbox.appwidget.CLEAN"
        const val REQUEST_CODE = 454923
    }

    private fun updateWidgets(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        appWidgetIds?.forEach {
            if (appWidgetManager?.getAppWidgetInfo(it)?.label == context?.getString(R.string.clear_cache)) {
                val views: RemoteViews = RemoteViews(context?.packageName, R.layout.widget_cleaner)
                        .apply {
                            setOnClickPendingIntent(R.id.cleaner_widget_clean,
                                    selfPendingIntent(context, ACTION_CLEAN))
                        }
                appWidgetManager?.updateAppWidget(it, views)
            }
        }
    }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        updateWidgets(context,appWidgetManager,appWidgetIds)
    }

    private fun selfPendingIntent(context: Context?, action: String) : PendingIntent {
        val intent: Intent = Intent(context, CleanerWidgetProvider::class.java)
        intent.action = action
        return PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        fun update() {
            val appWidgetManager: AppWidgetManager = AppWidgetManager.getInstance(context!!)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, GcamWidgetProvider::class.java))
            updateWidgets(context, appWidgetManager, appWidgetIds)
        }
        super.onReceive(context, intent)
        if (intent?.action.equals(ACTION_CLEAN)) {
            deleteCache(context?.packageName!!)
            update()
        }
        else if (intent?.action.equals(REFRESH_ACTION)) {
            update()
        }
    }
}