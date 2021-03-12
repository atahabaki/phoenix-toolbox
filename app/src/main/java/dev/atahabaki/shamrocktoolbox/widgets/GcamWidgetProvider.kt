package dev.atahabaki.shamrocktoolbox.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import dev.atahabaki.shamrocktoolbox.R

class GcamWidgetProvider : AppWidgetProvider() {
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
            }
        }
    }
}